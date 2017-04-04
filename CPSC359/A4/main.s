//////////////////////////////////////////////////////////////////////
// TODOs
// ~ Implement NewScene, DrawScene
//////////////////////////////////////////////////////////////////////

.section    .init
.globl     _start

_start:
    b       main

.section .text

//////////////////////////////////////////////////////////////////////
// Main
//////////////////////////////////////////////////////////////////////

main:
  MOV     sp, #0x8000 		// Initialize stack pointer
	BL	EnableJTAG 		// Enable JTAG
	BL	InitFrameBuffer 	// Initialize display: 1024x768
	BL 	InitSNES 		// Initialize SNES controller
	BL 	ClearScreen 		// Clear the screen

.globl mainMenu 			// Make label global
mainMenu:
	BL	DrawMenu  	    	// Start game by displaying the main menu
	BL 	StartMenuController  	// Enable SNES conroller in main menu
  CMP 	r0, #1 			// If StartMenuController returns 1, ...
	Beq 	exitGame 		// Exit game, else start game

.globl startGame			// Make label global
startGame:
	BL 	ResetGameState		// Reset game state
	BL 	SetupScene 		// Display new Scene
	BL 	gameController 		// Enable SNES controller in game
	CMP 	r0, #0 			// If gameController returns 0, player lost
	Beq 	mainMenu 		// Go to main menu
	CMP 	r0, #1 			// If gameController returns 1, player won
	Beq 	winGame
	Bne 	startGame 		// Else restart game

winGame:
	BL 	ClearScreen 		// ClearScreen
	MOV 	r0, #400
	MOV 	r1, #300
	LDR 	r2, =winString
	BL 	DrawString 		// DrawString(X, Y, exit string addr)
	B 	halt

exitGame:
	BL 	ClearScreen 		// ClearScreen
	MOV 	r0, #400
	MOV 	r1, #300
	LDR 	r2, =exitString
	BL 	DrawString 		// DrawString(X, Y, exit string addr)
halt:
	B	halt 			// Exit game


.section .data

.align 4
exitString: 	.asciz "Game Closed"
winString: 		.asciz "Game Won!"
