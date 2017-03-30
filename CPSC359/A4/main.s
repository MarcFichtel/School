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
	Beq 	exitGame 		// Exit game
.globl startGame			// Make function global
startGame:
	BL 	DrawScene 		// Display new Scene
	BL 	gameController 		// Enable SNES controller in game
	B 	mainMenu 		// After the game, go to main menu

exitGame:
	BL 	ClearScreen 		// ClearScreen	 
	MOV 	r0, #400
	MOV 	r1, #300
	LDR 	r2, =exitString	
	BL 	DrawString 		// DrawString(X, Y, string addr)
halt: 	
	B	halt 			// Exit game


.section .data

.align 4
exitString: 	.asciz "Game Closed"
