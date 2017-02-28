//////////////////////////////////////////////////////
// CPSC 359
// Assignment 3
// Students:
// * Marc-Andre Fichtel, 30014709
// * Cardin Chen, 10161477
//
// TODOs
// * Everything
//
// Program does the following:
// * Take input from a SNES controller
// * Display the input button
//
// Register usage
// *  
// * 
// * 
//////////////////////////////////////////////////////

.section 	.init
.global 	_start

_start:
	B 	main

//////////////////////////////////////////////////////
// Text Section
//////////////////////////////////////////////////////

.section 	.text

main:
	// Start program 
	MOV 	sp, #0x8000 						// Establish stack pointer
	BL 	EnableJTAG 						// Enable JTAG
	BL 	InitUART 						// Enable UART

	// Print names
	LDR 	r0, =authors 						// Load 'author' string into register
	BL 	stringBitCount 						// Branch to count bits & print
  
  
  
  
  
  
  
haltLoop$:
	B 	haltLoop$ 						// Terminate program with infinite loop

//////////////////////////////////////////////////////
// Print Message
//////////////////////////////////////////////////////

// Count number of bits to buffer for any given particular string
Print_Message: 
	PUSH 	{lr}							// Start function
	MOV 	r1, #0 							// Reset loop counter
	MOV 	r2, r0 							// Move print address into temporary register

loopCounter:
	LDRB 	r3, [r2], #1 						// Load byte into memory
	CMP 	r3, #0 							// Compare current byte to null
	ADDne 	r1, #1  						// If not equal, counter++, and
	Bne 	loopCounter 						// Loop
	BL 	WriteStringUART 					// Else, when done counting, print out r1 amount of buffer
	POP 	{pc} 							// End function

//////////////////////////////////////////////////////
// Initialize GPIO
//////////////////////////////////////////////////////

Init_GPIO:
  PUSH 	{lr}							        // Start function
  
  // Code here
  
  POP 	{pc} 							        // End function
  
//////////////////////////////////////////////////////
// Write to Latch
//////////////////////////////////////////////////////
  
Write_Latch:
  PUSH 	{lr}							        // Start function
  
  // Code here
  
  POP 	{pc} 							        // End function
  
//////////////////////////////////////////////////////
// Write to Clock
//////////////////////////////////////////////////////
  
Write_Clock:
  PUSH 	{lr}							        // Start function
  
  // Code here
  
  POP 	{pc} 							        // End function
  
//////////////////////////////////////////////////////
// Read Data
//////////////////////////////////////////////////////
  
Read_Data:
  PUSH 	{lr}							        // Start function
  
  // Code here
  
  POP 	{pc} 							        // End function
 
//////////////////////////////////////////////////////
// Wait
//////////////////////////////////////////////////////
 
Wait:
  PUSH 	{lr}							        // Start function
  
  // Code here
  
  POP 	{pc} 							        // End function
  
//////////////////////////////////////////////////////
// Read SNES
//////////////////////////////////////////////////////
  
Read_SNES:
  PUSH 	{lr}							        // Start function
  
  // Code here
  
  POP 	{pc} 							        // End function
  
//////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////

.section 	.data
.align

authors: 	.asciz 	"Created by Marc-Andre Fichtel and Cardin Chen\n\r"

button_press: 		.asciz 	"Please press a button...\n\r"
pressed_A: 			.asciz 	"You have pressed A \n\r"
pressed_B: 			.asciz 	"You have pressed B \n\r"
pressed_X: 			.asciz 	"You have pressed X \n\r"
pressed_Y: 			.asciz 	"You have pressed Y \n\r"
pressed_L: 			.asciz 	"You have pressed LEFT BUMPER \n\r"
pressed_R: 			.asciz 	"You have pressed RIGHT BUMPER \n\r"
pressed_joyRIGHT: 	.asciz 	"You have pressed Joy-pad RIGHT \n\r"
pressed_joyLEFT: 	.asciz 	"You have pressed Joy-pad LEFT \n\r"
pressed_joyUP: 		.asciz 	"You have pressed Joy-pad UP \n\r"
pressed_joyDOWN: 	.asciz 	"You have pressed Joy-pad DOWN \n\r"
pressed_START: 		.asciz 	"You have pressed START \n\r"
pressed_SELECT: 	.asciz 	"You have pressed SELECT \n\r"

end: 		.asciz "Program is terminating...\n\r"