//////////////////////////////////////////////////////////////////////
// 
// CPSC 359
// Assignment 4
// Cardin Chen: 10161477
// Olga Bogdanova: 
// Marc-Andre Fichtel: 3001409
//
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
	CMP 	r0, #2 			// If gameController returns 0, player quit
	Beq 	mainMenu 		// Go to main menu
	CMP 	r0, #1 			// If gameController returns 1, player restarted
	Beq 	startGame		// Restart game
	CMP 	r0, #0 			// if gameController returns 0, player won
	Beq 	winGame 		// Game won
	B 	gameOver 		// Else player lost

winGame:
	BL 	ClearScreen 		// ClearScreen
	MOV 	r0, #400
	MOV 	r1, #300
	LDR 	r2, =winString
	BL 	DrawString 		// DrawString(X, Y, string addr)
	B 	halt

gameOver:
	BL 	ClearScreen 		// ClearScreen
	MOV 	r0, #400
	MOV 	r1, #300
	LDR 	r2, =loseString
	BL 	DrawString 		// DrawString(X, Y, string addr)
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
winString: 	.asciz "Game Won!"
loseString: 	.asciz "Game Over!"
