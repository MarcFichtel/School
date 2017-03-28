////////////////////////////////////////////////////////////////////////////////////////
// TODOs:
// ~ Change DrawBoard and NewBoard functions	--> InGameMenuControl
// ~ Input mapping (which button does what) 	--> PlayerControl
////////////////////////////////////////////////////////////////////////////////////////

.section .text

////////////////////////////////////////////////////////////////////////////////////////
// Process in-game menu selection
// Outputs:
// ~ r0: 
// ~~~ 0 (player returned to game)
// ~~~ 1 (player restared game)
// ~~~ 2 (player quit game to main menu)
////////////////////////////////////////////////////////////////////////////////////////

.globl InGameMenuControl 		// Make function global
InGameMenuControl:
	PUSH	{lr} 			// Start function
	
gameMenuRead:	
	BL	longDelayLoop		// Delay to avoid user double click
	BL	ReadSNES		// ReadSNES()
	LDR	r1, =0xFFFF		// Prepare mask
	TEQ	r0, r1			// Test if any buttons are pressed
	Beq	gameMenuRead		// Loop if no buttons were pressed
	LDR	r1, =0xFFF7		// Mask for bit [3]
	TEQ	r0, r1			// Check if 'START' was pressed
	Beq	gameMenuStart 		// Branch to function for start press inside game menu
	LDR	r1, =0xFEFF		// Mask for bit [8]
	TEQ	r0, r1			// Check if 'A' was pressed 
	Beq	gameMenuA 		// Branch to function for A press inside game menu
	LDR	r1, =0xFFEF		// Mask for bit [4]
	TEQ	r0, r1			// Check if 'UP' was pressed
	Beq	gameMenuSelect	 	// Branch to function for UP press inside game menu
	LDR	r1, =0xFFDF		// Mask for bit [5]
	TEQ	r0, r1			// Check if 'DOWN' was pressed
	Beq	gameMenuSelect 		// Branch to function for DOWN press inside game menu
	B	gameMenuRead		// Else, loop
		
gameMenuStart:
	BL 	DrawBoard		// Redraw the board
	MOV	r0, #0			// Return #0 (player returned to game)
	B	gameMenuDone		// Finish function

gameMenuA:
	LDR	r0, =inGameSelected	// Load menu selection address
	LDR	r1, [r0]		// Load menu selection value
	CMP	r1, #0			// Check if Restart was chosen
	Bne	gameMenuQuit		// Else selection = Exit Game

gameMenuRestart:
	BL	NewBoard		// Reset the board
	BL	DrawBoard		// Draw the new board
	MOV	r0, #1			// Return #1 (player restarted game) 
	B	gameMenuDone		// Finish the method

gameMenuQuit:	
	BL	ClearScreen		// Clear the screen
	BL	DrawMenus		// Draw the main menu
	MOV	r0, #2			// Return #2 (player exited game)
	B	gameMenuDone		// Finish the method
	
gameMenuSelect:
	BL	MoveGameMenuSelect	// Move menu selector
	B	gameMenuLetGo		// Wait until no buttons are pressed
		
gameMenuLetGo:
	BL	Delay		// Delay to avoid user double click
	BL	ReadFromCont		// ReadSNES()
	LDR	r1, =0xFFFF		// Prepare mask
	TEQ	r0, r1			// Test if any buttons are pressed
	Bne	gameMenuLetGo$		// Loop if no buttons were pressed
	B	gameMenuControllerRead$ // Else, check which button was pressed
		
gameMenuDone:	
	POP	{pc} 			// End function

////////////////////////////////////////////////////////////////////////////////////////
// Move main menu selection
// Outputs:
// ~ r0: 
// ~~~ 0 (player started game)
// ~~~ 1 (player exited game)
////////////////////////////////////////////////////////////////////////////////////////	
	
.globl MainMenuControl 			// Make function global
MainMenuControl:
	PUSH	{lr} 			// Start function
	
mainMenuRead:	
	BL	Delay			// Delay to avoid user double click
	BL	ReadFromCont		// ReadSNES()
	LDR	r1, =0xFFFF		// Prepare mask
	TEQ	r0, r1			// Test if any buttons are pressed
	Beq	mainMenuRead		// Loop if no buttons were pressed
	LDR	r1, =0xFEFF		// Mask for bit [8]
	TEQ	r0, r1			// Check if 'A' was pressed 
	Beq	mainMenuA		// Branch to function for A press in main menu
	LDR	r1, =0xFFEF		// Mask for bit [4]
	TEQ	r0, r1			// Check if 'UP' was pressed
	Beq	mainMenuMoveSelect 	// Branch to function for UP press in main menu
	LDR	r1, =0xFFDF		// Mask for bit [5]
	TEQ	r0, r1			// Check if 'DOWN' was pressed
	Beq	mainMenuMoveSelect 	// Branch to function for DOWN press in main menu
	B	mainMenuRead		// Else, loop
		
mainMenuA:
	LDR	r0, =mainMenuSelected	// Load main menu selection address
	LDR	r1, [r0]		// Load main menu selection value
	CMP	r1, #0			// Check if Restart was selected
	Bne	mainMenuQuit		// Else, exit game

mainMenuStart:
	MOV	r0, #0		 	// Return #0 (player started game)		
	B	mainMenuDone 		// Finish function

mainMenuQuit:	
	BL	ClearScreen 		// Clear the screen
	MOV	r0, #1 			// Return 1 (player exited game)
	B	mainMenuDone 		// Finish function
	
mainMenuMoveSelect:
	BL	MainMenuSelect 		// Go to mve selection
	B	mainMenuLetGo 		// Finish function
		
mainMenuLetGo:
	BL	Delay			// Delay to avoid user double click
	BL	ReadFromCont		// ReadSNES()
	LDR	r1, =0xFFFF		// Prepare mask
	TEQ	r0, r1			// Test if any buttons are pressed
	Bne	mainMenuLetGo		// Loop until no buttons are pressed
	B	mainMenuRead	 	// Then loop for the next main menu input
		
mainMenuDone:	
	POP	{pc} 			// End function
	
////////////////////////////////////////////////////////////////////////////////////////
// Control Player Character in game
////////////////////////////////////////////////////////////////////////////////////////

.globl PlayerControl 			// Make function global
PlayerControl:
	PUSH	{lr} 			// Start function
	
pControl:
	BL	Delay			// Delay to avoid user double click
	BL	ReadSNES		// ReadSNES()
	LDR	r1, =0xFFFF		// Prepare mask
	TEQ	r0, r1			// Test if any buttons are pressed
	Beq	pControl		// Loop if no buttons are pressed
	LDR	r1, =0xFFF7		// Mask for bit [3]
	TEQ	r0, r1			// Check if 'START' was pressed
	Beq	startPress		// Branch to function for START press in game
	LDR	r1, =0xFEFF		// Mask for bit [8]
	TEQ	r0, r1			// Check if 'A' was pressed 
	Beq	aPress			// Branch to function for A press in game
	LDR	r1, =0xFFEF		// Mask for bit [4]
	TEQ	r0, r1			// Check if 'UP' was pressed
	Beq	upPress 		// Branch to function for UP press in game
	LDR	r1, =0xFFDF		// Mask for bit [5]
	TEQ	r0, r1			// Check if 'DOWN' was pressed
	Beq	downPress 		// Branch to function for DOWN press in game
	LDR	r1, =0xFFBF		// Mask for bit [6]
	TEQ	r0, r1			// Check if 'LEFT' was pressed
	Beq	leftPress 		// Branch to function for LEFT press in game
	LDR	r1, =0xFF7F		// Mask for bit [7]
	TEQ	r0, r1			// Check if 'RIGHT' was pressed
	Beq	rightPress 		// Branch to function for RIGHT press in game
	B	pControl		// Else, loop
			
startPress:
	BL	DrawInGameMenu		// Draw in-game menu
	BL	InGameMenuControl	// Go to in-game menu control
	CMP	r0, #0			// If return = 0, player pressed start
	Beq	letGo			// Continue game	
	CMP	r0, #1			// If return = 1, player restarted start
	Beq	pControl		// Continue game after it was restared	
	CMP	r0, #2			// If return = 1, player exited start
	Beq	mainMenu		// Go to main menu
	B 	letGo			// Wait until no buttons are pressed
		
aPress:
	BL	ActionKey		// TODO (A = Jump)
	B	letGo			// Wait until no buttons are pressed

upPress:
	BL	MoveUp			// TODO (UP = nohing, replace with B = SpeedUp)
	B	letGo			// Wait until no buttons are pressed

downPress:
	BL	MoveDown		// TODO (DOWN = Crouch)
	B	letGo			// Wait until no buttons are pressedd

leftPress:
	BL	MoveLeft		// TODO (LEFT = Move Left)
	B	letGo			// Wait until no buttons are pressed

rightPress:
	BL	MoveRight		// TODO (RIGHT = Move Right)
	B	letGo			// Wait until no buttons are pressed
		
letGo:
	BL	Delay			// Delay to avoid user double click
	BL	ReadSNES		// ReadSNES()
	LDR	r1, =0xFFFF		// Prepare mask
	TEQ	r0, r1			// Test if any buttons are pressed
	Bne	letGo			// Loop until no buttons are pressed
	B	pControl		// Then loop for the next game input
	
	POP 	{pc}			// End function
	
////////////////////////////////////////////////////////////////////////////////////////
// Delay (avoid user double click)
////////////////////////////////////////////////////////////////////////////////////////
	
.globl Delay 				// Make function global
Delay:
	PUSH	{r4-r5,lr} 		// Start function
	
	MOV	r4, #0			// Counter = 0
	LDR	r5, =100000 		// Set how often to loop (i.e. how long to wait)
delayLoop:
	ADD	r4, #1			// Increment to counter
	CMP	r4, r5			// Check whether to loop again 
	Blo	delayLoop		// Loop
	
	POP	{r4-r5,pc}		// End function

////////////////////////////////////////////////////////////////////////////////////////
// Long Delay (avoid user double click)
////////////////////////////////////////////////////////////////////////////////////////

.globl longDelayLoop 			// Make function global
longDelayLoop:
	PUSH	{r4-r5,lr} 		// Start function
	
	MOV	r4, #0			// Counter = 0
	LDR	r5, =1000000 		// Set how often to loop (i.e. how long to wait)
longDelayLoop:
	ADD	r4, #1			// Increment to counter
	CMP	r4, r5			// Check whether to loop again 
	Blo	longDelayLoop		// Loop
	
	POP	{r4-r5,pc}		// End function
