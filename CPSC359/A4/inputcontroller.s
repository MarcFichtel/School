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
	LDR 	r0, =startSelState  		// Load address of start selection state (in GameState.s)
	LDR 	r1, [r0]
	CMP	r1, #1 				// If EXIT is selected and A is pressed...
	Beq 	StartExitGame 			// Exit game
	MOV 	r0, #0 				// Else start game
	B 	startMenuDone 			// Return 0 in r0 (game was started)

// startSelState is in gamestate.s
// startSelState = 0 if start is selected [default]
// startSelState = 1 if exit is selected
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
//////////////////////////////////////////////////////////////////////////////////////
.globl gameController
gameController:
	PUSH	{lr}

luigiController:
	BL	ReadSNES			// Read user input
	LDR	r1, =0xFFFF			// Prepare mask
	TEQ	r0, r1				// Check if any buttons are pressed
	Beq	luigiController			// If not, loop to top

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

	B	luigiController			// If none of these were pressed, loop yo top

// TODO Make 2 functions: Drawing pause menu (in framebuffer.s), and enabling controls & processing input in it (in this file)
gamePaused:
//	BL	pauseMenu			// Draw in-game menu
//	BL	pauseController			// Enable controller in in-game menu
//	CMP	r0, #0				// If return = 0, START was pressed again
//	Beq	letGo				// Wait until no buttons are pressed, then resume game

//	CMP	r0, #1				// If return = 1 RESTART was pressed
//	Beq	playerControl$			// Redraw game, enable controller for it
//	CMP	r0, #2				// If return = 2 QUIT was pressed
//	Beq	mainMenu			// Go to main menu
//	B 	letGo				// Else, wait until no buttons are pressed

// TODO Make function to update state and redraw luigi
upPress:
//	bl	Jump				// Jump if possible
//	b	letGo				// Wait until no buttons are pressed

// TODO Make function to update state and redraw luigi
leftPress:
//	bl	MoveLeft			// Move left if possible
//	b	letGo				// Wait until no buttons are pressed

// TODO Make function to update state and redraw luigi
rightPress:
//	bl	MoveRight			// Move right if possible
//	b	letGo				// Wait until no buttons are pressed

letGo:
	BL	ReadSNES			// Read user input
	LDR	r1, =0xFFFF			// Prepare mask
	TEQ	r0, r1				// Check if any buttons were pressed
	Bne	letGo				// Wait until no buttons are pressed
	B	luigiController			// Check for next input

	POP {pc} 				// End function

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

	// TODO

	POP 	{pc} 				// End function

//////////////////////////////////////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////////////////////////////////////

.section .data

.align 4
startSelState: .int    0
