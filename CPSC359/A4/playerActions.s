///////////////////////////////////////////////////////////////////////////////////
// Player Actions
// ~ Jump
// ~ Move left
// ~ Move right
// ~ Die
// ~ UpdatePlayer
// ~ UpdateEnemies (TODO move this into different file)
//
// TODO In order for this to work we will need an update function that draws each moving
// object per update cycle (player & enemies). While the player is jumping, enemies
// should continue moving and the player should also be able to move sideways.
// This update function needs to check if the player is jumping via the game state,
// and if so, draw the player a bit higher. If the player is falling, draw the
// player a bit lower. If the player is jumping and has reached the max jump height,
// switch from jumping to falling.
//
// TODO Additionally, we will need a function to do collision checking with objects.
// ~ If the player touches floors and blocks, update the appropriate collision flags.
// ~ If a player hits a Goomba from the top, the Goomba should die (set Goomba dead flag).
// ~ If a player hits a Gooma from the side, the player should die (call die function).
// ~ If the player collides with a Boo, the player should die (call die function).
//
///////////////////////////////////////////////////////////////////////////////////

.section .text

///////////////////////////////////////////////////////////////////////////////////
//
// Jump
//
// The actual upwards movement is performed in UpdatePlayer.
// This method only starts the Jump by checking if its possible
// and setting maxJumpHeight. A Jump can only be started when a Jump
// is not already in progress (i.e. jump or fall flag are true).
//
///////////////////////////////////////////////////////////////////////////////////

.globl Jump 					// Make function global
Jump:
	PUSH	{r4-r5, lr} 			// Start function

	LDR 	r0, =state			// Load game state address
	LDR 	r1, [r0, #8] 			// Load player position Y
	MOV 	r2, #1 				// r2 = 1
	MOV 	r3, #0				// r3 = 0

	LDR 	r4, [r0, #20] 			// Load jump flag
	LDR 	r5, [r0, #24] 			// Load fall flag

	CMP 	r4, r2 				// If player is already jumping
	Beq 	jumpDone	 		// Do nothing (finish function)
 	CMP 	r5, r2 				// If player is falling
	Beq 	jumpDone	 		// Do nothing (finish function)

	STR 	r2, [r0, #20] 			// Else set Jump to true
	SUB 	r1, #256 			// Max jump height = Y - 256 (4 blocks)
	STR 	r1, [r0, #28] 			// Set max jump height in game state

jumpDone:
	POP 	{r4-r5, pc} 			// End function

///////////////////////////////////////////////////////////////////////////////////
//
// Move Left
//
// If both LEFT and RIGHT are pressed, moving RIGHT has priority
//
///////////////////////////////////////////////////////////////////////////////////

.globl MoveLeft					// Make function global
MoveLeft:
	PUSH	{lr} 				// Start function

	LDR 	r0, =state			// Load game state address
	LDR 	r4, [r0, #32] 			// Load moving left flag
	LDR 	r5, [r0, #36] 			// Load moving right flag
	MOV 	r1, #1 				// r1 = 1
	MOV 	r2, #0				// r2 = 0

 	CMP 	r5, r1 				// If player is already moving right
	Beq 	moveLeftDone	 		// Do nothing (finish function)

	CMP 	r4, r1 				// Else if player is already moving left
	Beq 	moveLeftDone	 		// Do nothing (finish function)

	STR 	r1, [r0, #32] 			// Else set moving left to true
	STR 	r2, [r0, #36] 			// And set moving right to false

moveLeftDone:
	POP 	{pc} 				// End function

///////////////////////////////////////////////////////////////////////////////////
//
// Move Right
//
// If both LEFT and RIGHT are pressed, moving RIGHT has priority
//
///////////////////////////////////////////////////////////////////////////////////

.globl MoveRight				// Make function global
MoveRight:
	PUSH	{lr} 				// Start function

	LDR 	r0, =state			// Load game state address
	LDR 	r4, [r0, #32] 			// Load moving left flag
	LDR 	r5, [r0, #36] 			// Load moving right flag
	MOV 	r1, #1 				// r1 = 1
	MOV 	r2, #0				// r2 = 0

 	CMP 	r4, r1 				// If player is already moving right
	Beq 	moveRightDone	 		// Do nothing (finish function)

	STR 	r2, [r0, #32] 			// Else set moving left to false
	STR 	r1, [r0, #36] 			// And set moving right to true

moveRightDone:
	POP 	{pc} 				// End function

///////////////////////////////////////////////////////////////////////////////////
//
// Die
//
// Outputs
// ~ r0:
// ~~~ 0 if lives remaining
// ~~~ 1 if no lives remaining
//
///////////////////////////////////////////////////////////////////////////////////

.globl Die					// Make function global
Die:
	PUSH	{lr} 				// Start function

	LDR 	r0, =state 			// Load state base address
	LDR 	r1, [r0, #64] 			// Load remaining lives

	CMP 	r1, #0 				// If player has no lives remaining
 	MOVeq 	r2, #1 				// r2 = 1
	STReq 	r2, [r0, #72] 			// Set lose flag to 1 (true)
	MOVeq 	r0, r2 				// Then return 1 (no lives remaining)
	Beq 	dieDone 			// Finish function

	SUB 	r1, #1 				// Else subtract a life
	STR 	r1, [r0, #64] 			// And store back into game state
	MOV 	r0, #0 				// Then return 0 (lives remaining)

dieDone:
	POP 	{pc} 				// End function


///////////////////////////////////////////////////////////////////////////////////
//
// UpdatePlayer
// ~ Draw player
// ~ Handle horizontal movement
// ~ Handle vertical movement
//
// Outputs
// ~ r0:
// ~~~ 0: Game Over
// ~~~ 1: Game Won
// ~~~ any other value: Resume Game
//
///////////////////////////////////////////////////////////////////////////////////

.globl UpdatePlayer 				// Make function global
UpdatePlayer:
	PUSH 	{r1-r9, lr} 			// Start function

	LDR 	r0, =state 			// Load game state address

	// Check if player won the game
	LDR 	r1, [r0, #68] 			// Load win flag
	CMP 	r1, #1 				// If player won the game
	MOVeq 	r0, #1	 			// Return 1 in r0 (game won)
	Beq 	updateDone 			// Then finish update

	// Check if player lost the game
	LDR 	r1, [r0, #72] 			// Load lose flag
	CMP 	r1, #1 				// If player lost the game
	MOVeq 	r0, #0	 			// Return 0 in r0 (gameover)
	Beq 	updateDone 			// Then finish update

	BL 	DrawPC				// Draw player
	BL 	CheckCollisions 		// Check collisions

	// Name a few registers
	pX 	.req r1
	leftF	.req r2
	rightF	.req r3
	lCol	.req r4
	rCol	.req r5
	mSpeed	.req r6
	set 	.req r8
	clr 	.req r9

	MOV 	set, #1
	MOV 	clr, #0

 	// Handle horizontal movement
	LDR 	pX, [r0, #4] 			// Load player position X
	LDR 	leftF, [r0, #32] 		// Load player moving left flag
	LDR 	rightF, [r0, #36]	 	// Load player moving right flag
	LDR 	lCol, [r0, #44]	 		// Load collision left flag
	LDR 	rCol, [r0, #40]	 		// Load collision right flag
	LDR 	mSpeed, [r0, #12]	 	// Load move speed

	CMP 	rightF, set 			// If player is moving right
	Beq 	updateRight 			// Go to updateRight

	CMP 	leftF, set 			// Else if player is moving left
	Beq	updateLeft 			// Go to update left

	B 	doneHoriz 			// Else update vertical movement

updateRight:
	CMP 	rCol, clr 			// If there's no collision on the right
	ADDeq 	pX, mSpeed 			// New position = X + speed
	B 	doneHoriz  			// Then process vertical movement

updateLeft:
	CMP 	lCol, clr 			// If there's no collision on the left
	SUB 	pX, mSpeed 			// New position = X - speed

doneHoriz:
	STReq 	pX, [r0, #4]			// Store new horiz. position back into game state
	// Unname a few registers
	.unreq pX
	.unreq leftF
	.unreq rightF
	.unreq lCol
	.unreq rCol
	.unreq mSpeed

	// Handle vertical movement
	pY 	.req r1
	jumpF	.req r2
	fallF	.req r3
	uCol	.req r4
	dCol	.req r5
	jSpeed	.req r6
	maxJH 	.req r7

	LDR 	pY, [r0, #8] 			// Load player position Y
	LDR 	jumpF, [r0, #20] 		// Load player jumping flag
	LDR 	fallF, [r0, #24]	 	// Load player falling flag
	LDR 	uCol, [r0, #48]	 		// Load collision up flag
	LDR 	dCol, [r0, #52]	 		// Load collision down flag
	LDR 	jSpeed, [r0, #16]	 	// Load jump speed
	LDR 	maxJH, [r0, #28]		// Load max jump height

	CMP 	dCol, set 			// If player is touching ground
	Beq 	grounded 			// Player is grounded
	Bne 	notGrounded 			// Else player is not grounded

	// Player is grounded
	// TODO Might be able to optimize this a bit more
grounded:
	// Jump = Fall = false: Nothing happens
	CMP 	jumpF, clr 			// If player is not jumping
	CMPeq 	fallF, clr 			// And not falling
	Beq 	updateDone 			// Finish function

	// Jump = 0, fall = 1: Clear fall
	CMP 	jumpF, clr 			// If player is not jumping
	CMPeq 	fallF, set 			// And falling
	STReq 	clr, [r0, #24] 			// Clear falling flag
	Beq 	updateDone 			// Finish function

	// Jump = 1, fall = 0: Switch (Max height reached, jump = 0, fall = 1)
	CMP 	pY, maxJH 			// If the maximum height has been reached
	STReq 	clr, [r0, #20] 			// Jump = false
	STReq 	set, [r0, #24] 			// Fall = true
	Beq 	updateDone 			// Finish function

	// Jump = 1, fall = 0: Move up (Max height not reached)
	SUB 	pY, jSpeed 			// New Y position = Y - jump speed
	STR 	pY, [r0, #8] 			// Store new pos back into game state
	Beq 	updateDone 			// Finish function

	// Player is not grounded
notGrounded:
	// Jump = 0, fall = 1: Move down (falling)
	CMP 	jumpF, set 			// Is player jumping or falling?
	SUBeq 	pY, jSpeed 			// Move up: New Y position = Y - jump speed
	ADDne 	pY, jSpeed 			// Move down: New Y position = Y + jump speed

	// Unname a few registers
	.unreq pY
	.unreq jumpF
	.unreq fallF
	.unreq uCol
	.unreq dCol
	.unreq jSpeed
	.unreq maxJH
	.unreq set
	.unreq clr

updateDone:
	POP 	{r1-r9, pc} 			// End function

///////////////////////////////////////////////////////////////////////////////////
//
// TODO UpdateEnemies
//
// Not sure if we can update all enemies at once or if we need to call
// this method for each enemy we want to update.
//
///////////////////////////////////////////////////////////////////////////////////

.globl UpdateEnemies 				// Make function global
UpdateEnemies:
	PUSH 	{lr} 			// Start function

	POP 	{pc} 			// End function

///////////////////////////////////////////////////////////////////////////////////
//
// TODO Check Collisions
//
// Check Collisions with objects and update appropriate collision flags
// Check collisions with enemies and call appropriate die function (for player or enemy)
// Should also check if player is below the screen, in which case he dies --> call die function
//
///////////////////////////////////////////////////////////////////////////////////
