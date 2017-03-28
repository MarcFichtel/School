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
    	MOV     sp, #0x8000 	// Initialize stack pointer
	BL	EnableJTAG 	// Enable JTAG
	BL	InitFrameBuffer // Initialize display: 1024x768
	BL 	InitSNES 	// Initialize SNES controller
	BL 	ClearScreen 	// Clear the screen

.globl mainmenu 		// Make function global
mainMenu:
	BL	DrawMenu  	// Start game by displaying the main menu
	BL 	MainMenuControl // Enable SNES conroller in main menu
	CMP 	r0, #1 		// If main menu control returns 1, ...
	Beq 	halt 		// Player exited the game

.globl startGame		// Make function global
startGame:
	BL 	NewScene 	// Start new Scene
	BL 	DrawScene 	// Display new Scene
	BL 	PlayerControl 	// Enable SNES controller in game

halt: 	B	halt 		// Exit game
