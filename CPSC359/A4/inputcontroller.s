///////////////////////////////////////////////////////////////////////////////////
// Game control stuff
///////////////////////////////////////////////////////////////////////////////////

.section .text

///////////////////////////////////////////////////////////////////////////////////
// StartMenuController:
// Gets user input from controller and determines whether to start the game or
// exit the program
// Return in r0:
// #0 = start game
// #1 = quit game
///////////////////////////////////////////////////////////////////////////////////
.globl StartMenuController
StartMenuController:
	PUSH 	{lr}				// Start function

readStartInput:
	BL 	ReadSNES 			// Read user input
	LDR 	r1, =0xFFFF 			// Prepare mask
	TEQ 	r0, r1 				// If no buttons were pressed
	Beq 	readStartInput 			// Re-read controller input
	LDR 	r1, =0xFEFF  			// 1111 1110 1111 1111 = 'A'
	TEQ 	r0, r1 				// If A was pressed
	Beq 	StartMenuAPressed 		// Branch here
	LDR 	r1, =0xFFEF 			// 1111 1111 1110 1111 = 'UP'
	TEQ 	r0, r1  			// If UP was pressed
	Beq 	StartMenuToggle 		// Toggle menu selector
	LDR 	r1, =0xFFDF 			// 1111 1111 1101 1111 = 'DOWN'
	TEQ 	r0, r1 				// If DOWN was pressed
	Beq 	StartMenuToggle 		// Toggle menu selector
	B 	readStartInput 			// Else, loop

StartMenuAPressed:
	LDR 	r0, =startSelState  		// Load start selection state address
	LDR 	r1, [r0] 			// Load start selection state value
	CMP	r1, #1 				// If EXIT is selected and A is pressed...
	Beq 	StartExitGame 			// Exit game
	MOV 	r0, #0 				// Else start game
	B 	startMenuDone 			// Return 0 in r0 (game was started)

// startSelState: 0 = start, 1 = exit
StartMenuToggle:
	LDR 	r0, =startSelState 		// Load startSelState address
	LDRB 	r1, [r0] 			// Load startSelState value
	CMP 	r1, #0 				// If startSelState = 0...
	Beq 	exitT 				// Switch to 1, else...
	B 	startT 				// Switch to 0

	// Move selector to exit
exitT:
	MOV 	r1, #1
	STR 	r1, [r0] 			// Store 1 back into startSelState
	MOV 	r0, #352
	MOV 	r1, #500
	LDR 	r2, =menuPointerEXIT
	BL 	DrawStartMenu 			// Draw start menu options with exit selected
	B 	doneT 				// Finish function


	// Move selector to start
startT:
	MOV 	r1, #0
	STR 	r1, [r0]			// Store 0 back into startSelState
	MOV 	r0, #352
	MOV 	r1, #500
	LDR 	r2, =menuPointerSTART
	BL 	DrawStartMenu 			// Draw start menu options with start selected

doneT:
	B 	readStartInput 			// Loop back to top (read input)

StartExitGame:
	BL 	ClearScreen 			// Clear screen
	MOV 	r0, #1 				// Return 1 in r0 (game was exited)
	B 	startMenuDone 			// Finish function

startMenuDone: 					// Return to main
	POP 	{pc} 				// End function

//////////////////////////////////////////////////////////////////////////////////////
// Game Controller
// Processes the following user inputs:
// ~ LEFT: Walk left
// ~ RIGHT: Walk right
// ~ UP: Jump
// ~ START: Pause game, open pause menu
// TODO Update enemies, fix luigi
//////////////////////////////////////////////////////////////////////////////////////
.globl gameController
gameController:
	PUSH	{lr}

luigiController:
	BL	ReadSNES			// Read user input

	LDR	r1, =0xFFF7			// Mask for START
	TEQ	r0, r1				// Check if START was pressed
	Beq	gamePaused 			// If so, go to gamePaused

	LDR	r1, =0xFFEF			// Mask for UP
	TEQ	r0, r1				// Check if UP was pressed
	Beq	upPress 			// If so, go to upPress

	LDR	r1, =0xFFBF			// Mask for LEFT
	TEQ	r0, r1				// Check if LEFT was pressed
	Beq	leftPress 			// If so, go to leftPress

	LDR	r1, =0xFF7F			// Mask for RIGHT
	TEQ	r0, r1				// Check if RIGHT was pressed
	Beq	rightPress 			// If so, go to rightPress

	// Update player each frame, even if no buttons were pressed
update:
	BL 	UpdatePlayer 			// Update player
	CMP 	r0, #0 				// If player won the game
	Beq 	doneControl 			// Return 0 to main (game won, go to main)
	CMP 	r0, #1 				// If player lost the game
	Beq 	doneControl 			// Return 1 to main (game over, go to main)

// TODO BL 	UpdateEnemies 			// Update enemies

	B	luigiController			// Loop to top

gamePaused:
	BL	DrawPauseMenuOptions		// Draw in-game menu // draw in
	BL	pauseController			// Enable controller in in-game menu
	CMP	r0, #0				// If return = 0, START was pressed again
	// *** TODO: remove the pause menu by updating the map

	Beq 	update 				// Resume game
	CMP	r0, #1				// Else if return = 1 RESTART was pressed
	Beq	doneControl			// Redraw game, enable controller for it
	CMP	r0, #2				// Else if return = 2 QUIT was pressed
	Beq	doneControl			// Go to main menu

upPress:
	BL	Jump				// Jump if possible
	B 	update 				// Then go to update

leftPress:
	BL	MoveLeft			// Move left if possible
	B 	update 				// Then go to update

rightPress:	
	BL	MoveRight			// Move right if possible (then go to update)
	B 	update 				// Then go to update

doneControl:
	POP 	{pc} 				// End function

//////////////////////////////////////////////////////////////////////////////////////
// Pause Menu Controller
// Processes the following user inputs:
// ~ UP: Toggle pause menu selection
// ~ DOWN: Toggle pause menu selection
// ~ A: Select option
// ~ START: Close pause menu, resume game
//////////////////////////////////////////////////////////////////////////////////////

.globl pauseController
pauseController:
	PUSH	{lr}

readPauseInput:
	BL 	ReadSNES 				// Read user input
	LDR 	r1, =0xFFFF 			// Prepare mask
	TEQ 	r0, r1 				// If no buttons were pressed
	Beq 	readPauseInput 			// Re-read controller input
	LDR 	r1, =0xFEFF  			// Mask for A
	TEQ 	r0, r1 				// If A was pressed
	Beq 	PauseMenuAPressed 		// Branch here
	LDR 	r1, =0xFFEF 			// Mask for UP
	TEQ 	r0, r1  			// If UP was pressed
	Beq 	PauseMenuToggle 		// Toggle menu selector
	LDR 	r1, =0xFFDF 			// Mask for DOWN
	TEQ 	r0, r1 				// If DOWN was pressed
	Beq 	PauseMenuToggle 		// Toggle menu selector
	LDR	r1, =0xFFF7			// Mask for START
	TEQ	r0, r1				// If START was pressed
	Beq	gameUnpaused 			// Resume game play
	B 	readPauseInput 			// Else, loop

gameUnpaused:
	MOV 	r0, #0 				// Return = 0 (game was unpaused)
	B 	pauseMenuDone 			// Finish function

PauseMenuAPressed:
	LDR 	r0, =pauseSelState  		// Load pause selection state address
	LDR 	r1, [r0]			// Load pause selection state value
	CMP		r1, #1 				// If EXIT is selected and A is pressed...
	Beq 	PauseExit 			// Exit game (go to main menu)
	MOV 	r0, #1 				// Else restart game
	B 	pauseMenuDone 			// Return 1 in r0 (game was restarted)

	// pauseSelState: 0 = restart, 1 = exit
PauseMenuToggle:
	LDR 	r0, =pauseSelState 		// Load pauseSelState address
	LDRB 	r1, [r0] 			// Load pauseSelState value
	CMP 	r1, #0 				// If pauseSelState = 0...
	Beq 	exitP 				// Switch to 1, else...
	B 	startP 				// Switch to 0

	// Move selector to exit
exitP:
	MOV 	r1, #1
	STR 	r1, [r0] 			// Store 1 back into pauseSelState
	MOV 	r0, #352
	MOV 	r1, #500
	LDR 	r2, =pausePointerEXIT 		// Draw pause menu options with exit selected
	B 	doneP 				// Finish function

	// Move selector to restart
startP:
	MOV 	r1, #0
	STR 	r1, [r0]			// Store 0 back into pauseSelState
	MOV 	r0, #352
	MOV 	r1, #500
	LDR 	r2, =pausePointerRESTART		// Draw pause menu options with restart selected

doneP:
	B 	readPauseInput 			// Loop back to top (read input)

PauseExit:
	MOV 	r0, #1 				// Return 1 in r0 (game was exited, go to main menu)

pauseMenuDone: 					// Return to main
	POP 	{pc} 				// End function

//////////////////////////////////////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////////////////////////////////////

.section .data

.align 4
startSelState: 	.int    0 			// Start menu selection state
pauseSelState: 	.int 	0 			// Pause menu selection state
