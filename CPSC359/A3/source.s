//////////////////////////////////////////////////////
// CPSC 359
// Assignment 3
// Students:
// * Marc-Andre Fichtel, 30014709
// * Cardin Chen, 10161477
//
// TODOs
// * Read_SNES
//
// Program does the following:
// * Take input from a SNES controller
// * Display the input button
//
// Register usage
// * r5 is used to track which buttons on the SNES controller are pressed
// * 
// * VERSION MONDAY 3 26 AM 
//////////////////////////////////////////////////////

.section 	.init
.global 	_start

_start:
	B 	main

buttons_r 	.req r5

//////////////////////////////////////////////////////
// Text Section
//////////////////////////////////////////////////////

.section 	.text

main:
	// Start program 
	MOV 	sp, #0x8000 						// Establish stack pointer
	BL 	EnableJTAG 						// Enable JTAG
	BL 	InitUART 						// Enable UART
	
	// Set up GPIO lines 9, 10, 11
	MOV 	r0, #9 							// 1st arg: 9 (GPIO line)
	MOV 	r1, #1 							// 2nd arg: 1 (function code output)
	BL 	Init_GPIO 						// Set up GPIO9
	
	MOV 	r0, #10 						// 1st arg: 10 (GPIO line)
	MOV 	r1, #1							// 2nd arg: 1 (function code output)
	BL 	Init_GPIO 						// Set up GPIO10
	
	MOV 	r0, #11 						// 1st arg: 11 (GPIO line)
	MOV 	r1, #0 							// 2nd arg: 0 (function code input)
	BL 	Init_GPIO 						// Set up GPIO11

	// Print names
	LDR 	r0, =authors 						// Load 'author' string into register
	BL 	Print_Message 						// Branch to count bits & print
	
mainLoop:
	// Print input instruction
	LDR 	r0, =button_press 					// Load input prompt string into register
	BL 	Print_Message 						// Branch to count bits & print
  	B Read_SNES
  
haltLoop$:
	B 	haltLoop$ 						// Terminate program with infinite loop

//////////////////////////////////////////////////////
// Print Message
// Inputs:
// ~ r0: Address of string to be printed
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
// *** Prints Pressed Button 
// Inputs:
// *** 
//////////////////////////////////////////////////////
Print_Button:
	PUSH 	{lr}
  	LDR 	r0, =button_message 		// load button message strings 
	MOV 	r1, #34  					// buffer: 34 bits, each string in button_message is 34 bits 
	MUL  	r2, r1, buttons_r			// r2 = index multiplied by returned button-press value
	ADD 	r0, r2 						// update r0 with index of button to be printed 
	bl 		WriteStringUART 			// print designated button message 
	CMP 	buttons_r, #102 			// *** check if this is correct: if start button is pressed, exit 
	// *** logic: (34 bits per string) * (3, the start button value) = 102 
	Beq 	StartButton_Pressed 		// if start button is pressed, exit 
  	POP 	{pc}

 StartButton_Pressed:
 	ldr 	r0, =endMsg 
 	BL 		Print_Message
 	b 		haltLoop 
  	// *** added another pop{pc} in startbutton_pressed just in case of memory errors if not popped, most likly unneccesary
  	POP 	{pc}
//////////////////////////////////////////////////////
// Initialize GPIO
// Inputs: 
// ~ r0: GPIO line to be initialized
// ~ r1: Function code to initialize the line to
//////////////////////////////////////////////////////

Init_GPIO:
  	PUSH 	{lr}						        // Start function
	
	// Check which GPIO line is to be initialized
  	CMP 	r0, #9 							// If GPIO9 is being initialized
  	Beq 	latch 							// Go to latch
  	CMP 	r0, #10 						// If GPIO10 is being initialized
	Beq 	data 							// Go to data
	CMP 	r0, #11 						// If GPIO11 is being initialized
	Beq 	clock 							// Go to clock
  
latch: 	// Init LATCH (Set GPIO9 to output)
	CMP 	r1, #1							// If function code != 1
	Bne 	end 							// Do nothing (go to end), else...
	LDR 	r0, =0x3F000000 					// Address for GPFSEL0
	LDR 	r1, [r0]						// Copy GPFSEL0 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	LSL 	r2, #27 						// Index of 1st bit for pin9
	BIC 	r1, r2 							// Clear pin9 bits
	MOV 	r3 , #1 						// Output function code
	LSL 	r3, #27 						// Shift 1 (output) over for pin9
	ORR 	r1, r3 							// Set pin9 function in r1 to 1 (output)
	STR 	r1, [r0] 						// Write back to GPFSEL0
  	B 	end 							// When init is complete, go to end	
	
data:	// Init DATA (Set GPIO10 to input)
  	CMP 	r1, #0							// If function code != 0
	Bne 	end 							// Do nothing (go to end), else...
	LDR 	r0, =0x3F000004 					// Address for GPFSEL1
	LDR 	r1, [r0]						// Copy GPFSEL1 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	BIC 	r1, r2 							// Clear pin10 bits
	ORR 	r1, r3 							// Set pin10 function in r1 to 0 (input)
	STR 	r1, [r0] 						// Write back to GPFSEL1
  	B 	end 							// When init is complete, go to end

clock: 	// Init CLOCK (Set GPIO11 to output)
	CMP 	r1, #1							// If function code != 1
	Bne 	end 							// Do nothing (go to end), else...
	LDR 	r0, =0x3F000004 				// Address for GPFSEL1
	LDR 	r1, [r0]						// Copy GPFSEL1 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	LSL 	r2, #3 							// Index of 1st bit for pin11, r2 = 0 111 000
	BIC 	r1, r2 							// Clear pin11 bits
	MOV 	r3 , #1 						// Output function code
	LSL 	r3, #3 							// Shift 1 (output) over for pin11
	ORR 	r1, r3 							// Set pin11 function in r1 to 1 (output)
	STR 	r1, [r0] 						// Write back to GPFSEL1
  	B 	end 								// When init is complete, go to end
	
end:
  	POP 	{pc} 						        // End function
  
//////////////////////////////////////////////////////
// Write to Latch
// Inputs:
// ~ r1: Bit to write to latch line
//////////////////////////////////////////////////////
  
Write_Latch:
  	PUSH 	{lr}						        // Start function
  
  	MOV 	r0, #9 							// r0 = 9 (for GPIO9)
  	LDR 	r2, =0x3F000000 					// Address for GPFSEL0
  	MOV 	r3, #1
  	LSL 	r3, r0 							// Align bit for pin 9
  	Teq	r1, #0 
  	
	STReq 	r3, [r2, #40] 						// GPCLR0
	STRne 	r3, [r2, #28] 						// GPSET0
	
  	POP 	{pc} 						        // End function
  
//////////////////////////////////////////////////////
// Write to Clock
// Inputs:
// ~ r1: Bit to write to clock line
//////////////////////////////////////////////////////
  
Write_Clock:
  	PUSH 	{lr}						        // Start function
  
  	MOV 	r0, #11 						// r0 = 11 (for GPIO11)
  	LDR 	r2, =0x3F000004 					// Address for GPFSEL1
  	MOV 	r3, #1
  	LSL 	r3, r0 							// Align bit for pin 11
  	Teq		r1, #0 
  	
	STReq 	r3, [r2, #40] 						// GPCLR0
	STRne 	r3, [r2, #28] 						// GPSET0
  
  	POP 	{pc} 						        // End function
  
//////////////////////////////////////////////////////
// Read Data
// Returns:
// ~ The read bit (0 or 1)
//////////////////////////////////////////////////////
  
Read_Data:
	PUSH 	{lr}							// Start function

	MOV 	r0, #10 						// r0 = 10 (for GPIO10)
	LDR 	r2, =0x3F000004 					// Address for GPFSEL1
	LDR 	r1, [r2, #52] 						// GPLEV0
	MOV 	r3, #1
	LSL 	r3, r0 							// Align pin10 bit
	AND 	r1, r3 							// Mask everything else
	Teq 	r1, #0
	
	MOVeq 	r4, #0 							// Return 0 if equal
	MOVne 	r4, #1 							// Return 1 if not equal

	POP 	{pc} 							// End function
 
//////////////////////////////////////////////////////
// Wait
// Inputs:
// ~ r0: Time to be waited in microseconds
//////////////////////////////////////////////////////
 
Wait:
  	PUSH 	{lr}							// Start function
  
	LDR 	r2, =0x3F003004 					// Address of CLO
	LDR 	r1, [r2] 						// Read CLO
	ADD 	r1, r0 							// Add time to be waited in micros
waitLoop:
	LDR 	r0, [r2] 						// Load CLO
	CMP 	r1, r0 							// If CLO < r1
	Bhi 	waitLoop 						// Go to waitLoop
  
  	POP 	{pc} 							// End function
  
//////////////////////////////////////////////////////
// Read SNES
// Returns:
// ~ Code of a pressed button in a register
//////////////////////////////////////////////////////
  
Read_SNES:
	PUSH 	{lr}							// Start function
  
  	MOV 	buttons_r, #0 						// Register for sampling buttons
	
	MOV 	r1, #1
	BL 	Write_Clock						// writeGPIO(CLOCK,#1)
  
  	MOV 	r1, #1
	BL 	Write_Latch						// writeGPIO(LATCH,#1)
  
  	MOV 	r0, #12
	BL 	Wait 							// wait(12ms) - signal to SNES to sample buttons
  
    MOV 	r1, #0 						 
	BL 	Write_Latch						// writeGPIO(LATCH,#0)
  
  pulseLoop:
  	MOV 	r2, #0 							// i = 0
	
	MOV 	r0, #6 							// r0 = 6ms 
	BL 	Wait  							// wait(6ms)
	
	MOV 	r1, #0
	BL 	Write_Clock						// writeGPIO(CLOCK,#0) - falling edge
  
  	MOV 	r0, #6
	BL 	Wait  							// wait(6ms)
	
	BL 	Read_Data 			// readGPIO(DATA, b) - read bit i
	CMP 	r4, #1 			// buttons[i] = b   
	MOVNE 	buttons_r, r2  	// if button is pressed (r2 == 0 right? ***) move index into buttons_r
	BL 		Print_Button 	// print button pressed 
	ADDEQ 	r2, #1 			// i ++ if button not pressed 
	
	MOV 	r1, #1
	BL 	Write_Clock			// writeGPIO(CLOCK,#1) - rising edge; new cycle
 	CMP 	r2, #16 
  	BLT 	pulseLoop		// if (i< 16) branch pulseLoop 
  	POP 	{pc} 			// End function
  
//////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////

.section 	.data
.align

authors: 		.asciz 	"Created by Marc-Andre Fichtel and Cardin Chen\n\r"

button_press: 		.asciz 	"Please press a button...\n\r"

endMsg: 			.asciz "Program is terminating...\n\r"

button_message: 	.asciz 	"You have pressed B \n\r           "

							, "You have pressed Y \n\r           "

							, "You have pressed SELECT \n\r      "

							, "You have pressed START \n\r       "

							, "You have pressed UP \n\r          "

							, "You have pressed DOWN \n\r        "

							, "You have pressed LEFT \n\r        "

							, "You have pressed RIGHT \n\r       "

							, "You have pressed A \n\r           "

							, "You have pressed X \n\r           "

							, "You have pressed LEFT BUMPER \n\r"

							, "You have pressed RIGHT BUMPER \n\r" // each string is aligned to 34 bits 
