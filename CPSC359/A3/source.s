//////////////////////////////////////////////////////
// CPSC 359
// Assignment 3
// Students:
// * Marc-Andre Fichtel, 30014709
// * Cardin Chen, 10161477
//
// TODOs
// * 
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
	BL 	Print_Message 						// Branch to count bits & print
  
  	// Initialize GPIO
	BL 	Init_GPIO 						// Set up GPIO
  
  
  
  
  
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
  	PUSH 	{lr}						        // Start function
  
  	// Init CLOCK (Set GPIO11 to output)
	LDR 	r0, =0x3F000004 					// Address for GPFSEL1
	LDR 	r1, [r0]						// Copy GPFSEL1 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	LSL 	r2, #3 							// Index of 1st bit for pin11, r2 = 0 111 000
	BIC 	r1, r2 							// Clear pin11 bits
	MOV 	r3 , #1 						// Output function code
	LSL 	r3, #3 							// Shift 1 (output) over for pin11
	ORR 	r1, r3 							// Set pin11 function in r1 to 1 (output)
	STR 	r1, [r0] 						// Write back to GPFSEL1
  
  	// Init LATCH (Set GPIO9 to output)
	LDR 	r0, =0x3F000000 					// Address for GPFSEL0
	LDR 	r1, [r0]						// Copy GPFSEL0 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	LSL 	r2, #27 						// Index of 1st bit for pin9
	BIC 	r1, r2 							// Clear pin9 bits
	MOV 	r3 , #1 						// Output function code
	LSL 	r3, #27 						// Shift 1 (output) over for pin9
	ORR 	r1, r3 							// Set pin9 function in r1 to 1 (output)
	STR 	r1, [r0] 						// Write back to GPFSEL0
	
	// Init DATA (Set GPIO10 to input)
  	LDR 	r0, =0x3F000004 					// Address for GPFSEL1
	LDR 	r1, [r0]						// Copy GPFSEL1 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	BIC 	r1, r2 							// Clear pin10 bits
	ORR 	r1, r3 							// Set pin10 function in r1 to 0 (input)
	STR 	r1, [r0] 						// Write back to GPFSEL1
  
  
  	POP 	{pc} 						        // End function
  
//////////////////////////////////////////////////////
// Write to Latch
//////////////////////////////////////////////////////
  
Write_Latch:
  	PUSH 	{lr}						        // Start function
  
  	// Code here
  
  	POP 	{pc} 						        // End function
  
//////////////////////////////////////////////////////
// Write to Clock
//////////////////////////////////////////////////////
  
Write_Clock:
  	PUSH 	{lr}						        // Start function
  
  	// Code here
  
  	POP 	{pc} 						        // End function
  
//////////////////////////////////////////////////////
// Read Data
//////////////////////////////////////////////////////
  
Read_Data:
	  PUSH 	{lr}							// Start function

	  // Code here

	  POP 	{pc} 							// End function
 
//////////////////////////////////////////////////////
// Wait
//////////////////////////////////////////////////////
 
Wait:
  	PUSH 	{lr}							// Start function
  
	LDR 	r0, =0x3F003004 					// Address of CLO
	LDR 	r1, [r0] 						// Read CLO
	ADD 	r1, #12 						// Add 12 micros
waitLoop:
	LDR 	r2, [r0] 						// Load CLO
	CMP 	r1, r2 							// If CLO < r1
	Bhi 	waitLoop 						// Go to waitLoop
  
  	POP 	{pc} 							// End function
  
//////////////////////////////////////////////////////
// Read SNES
//////////////////////////////////////////////////////
  
Read_SNES:
	PUSH 	{lr}							// Start function
  
  	// Code here
  
  	POP 	{pc} 							// End function
  
//////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////

.section 	.data
.align

authors: 		.asciz 	"Created by Marc-Andre Fichtel and Cardin Chen\n\r"

button_press: 		.asciz 	"Please press a button...\n\r"
pressed_A: 		.asciz 	"You have pressed A \n\r"
pressed_B: 		.asciz 	"You have pressed B \n\r"
pressed_X: 		.asciz 	"You have pressed X \n\r"
pressed_Y: 		.asciz 	"You have pressed Y \n\r"
pressed_L: 		.asciz 	"You have pressed LEFT BUMPER \n\r"
pressed_R: 		.asciz 	"You have pressed RIGHT BUMPER \n\r"
pressed_joyRIGHT: 	.asciz 	"You have pressed Joy-pad RIGHT \n\r"
pressed_joyLEFT: 	.asciz 	"You have pressed Joy-pad LEFT \n\r"
pressed_joyUP: 		.asciz 	"You have pressed Joy-pad UP \n\r"
pressed_joyDOWN: 	.asciz 	"You have pressed Joy-pad DOWN \n\r"
pressed_START: 		.asciz 	"You have pressed START \n\r"
pressed_SELECT: 	.asciz 	"You have pressed SELECT \n\r"

end: 			.asciz "Program is terminating...\n\r"
