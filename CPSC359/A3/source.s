//////////////////////////////////////////////////////
// CPSC 359
// Assignment 3
// Students:
// * Marc-Andre Fichtel, 30014709
// * Cardin Chen, 10161477
//
// TODOs
// * Get it to run
//
// Program does the following:
// * Take input from a SNES controller
// * Display the input button
//
// Register usage
// * r4 will be used as a counter
// * r5 is used to track which buttons on the SNES controller are pressed
// * r6 and r7 are used to temporarily hold and work with the button samplings
// * r8 is used to track the previous button sampling
//////////////////////////////////////////////////////

.section 	.init
.global 	_start

_start:
	B 	main

counter_r 	.req r4
buttons_r 	.req r5
temp1_r 	.req r6
temp2_r 	.req r7
btnsOld_r 	.req r8

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

	// Let button state = 0
	MOV 	buttons_r, #0

mainLoop:

	LDR 	r0, =button_press 					// Load input prompt string into register
	BL 	Print_Message 						// Branch to count bits & print

Read_Buttons:                                    
	BL      Read_SNES                        			// Returns the 16 bits in r0
	MOV 	buttons_r, r0 						// Move result into buttons_r	
	CMP     buttons_r,btnsOld_r                    			// Checks if the state is same as last
	Beq     Same_State                       			// Wait before checking for the next input
	MOV     btnsOld_r,buttons_r                          		// Keep track of the state
	MOV     temp1_r, buttons_r                          		// Move button sampling to r6
	MOV     counter_r, #0                          			// Initalize a counter
	MOV     buttons_r, #1                          			// buttons_r = 1
	MVN     buttons_r, buttons_r                          		// Reverse buttons_r to 0b1111111110

Check_Pressed_Buttons:
	BIC     temp2_r, temp1_r, buttons_r                      	// Bit-clear the register to check
	CMP     temp2_r, #0                          			// Check if any button is pressed
	Beq     Button_Pressed                   			// If a button is pressed, go to print it
	LSR	temp1_r, #1                          			// Else check the next bit
	ADD	counter_r, #1 						// counter++
	CMP	counter_r, #11                         			// If (counter >= 12)
	Ble	Check_Pressed_Buttons 					// Check pressed buttons
	B       Read_Buttons 						// Else loop

Button_Pressed:
	MOV 	r0, counter_r 						// Print the i-th pressed button
	BL 	Print_Button 						// Print out pressed button
	B     	mainLoop 	    		            		// Prompt user for next input

Same_State:
	ADD     r0, #30                         			// wait(30)
	BL      Wait                            			// Wait 30ms
	B       Read_Buttons                     			// Branches back to read the buttons

haltLoop:
	B 	haltLoop 						// Terminate program with infinite loop

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
// Prints Pressed Button
// Inputs:
// ~ r0: The button index to be printed
//////////////////////////////////////////////////////
Print_Button:
	PUSH 	{lr} 							// Start function

	LDR 	r3, =button_message 					// Load button message strings address
	MOV 	r1, #34  						// Buffer: 34 bits, each string in button_message is 34 bits
	MUL  	r2, r1, r0						// r2 = button index * 34
	ADD 	r3, r2 							// String array base address + offset = String to be printed
	MOV 	r0, r3 							// Move string address into r0 (arg to print function)
	BL 	WriteStringUART 					// Print designated button message

  	POP 	{pc} 							// End function

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
	LDR 	r0, =0x3F200000 					// Address for GPFSEL0
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
	LDR 	r0, =0x3F200004 					// Address for GPFSEL1
	LDR 	r1, [r0]						// Copy GPFSEL1 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	BIC 	r1, r2 							// Clear pin10 bits
	ORR 	r1, r3 							// Set pin10 function in r1 to 0 (input)
	STR 	r1, [r0] 						// Write back to GPFSEL1
  	B 	end 							// When init is complete, go to end

clock: 	// Init CLOCK (Set GPIO11 to output)
	CMP 	r1, #1							// If function code != 1
	Bne 	end 							// Do nothing (go to end), else...
	LDR 	r0, =0x3F200004 					// Address for GPFSEL1
	LDR 	r1, [r0]						// Copy GPFSEL1 into r1
	MOV 	r2, #7 							// (r2 = 0b111)
	LSL 	r2, #3 							// Index of 1st bit for pin11, r2 = 0 111 000
	BIC 	r1, r2 							// Clear pin11 bits
	MOV 	r3 , #1 						// Output function code
	LSL 	r3, #3 							// Shift 1 (output) over for pin11
	ORR 	r1, r3 							// Set pin11 function in r1 to 1 (output)
	STR 	r1, [r0] 						// Write back to GPFSEL1
  	B 	end 							// When init is complete, go to end

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
  	LDR 	r2, =0x3F200000 					// Address for GPFSEL0
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
  	LDR 	r2, =0x3F200004 					// Address for GPFSEL1
  	MOV 	r3, #1
  	LSL 	r3, r0 							// Align bit for pin 11
  	Teq		r1, #0

	STReq 	r3, [r2, #40] 						// GPCLR0
	STRne 	r3, [r2, #28] 						// GPSET0

  	POP 	{pc} 						        // End function

//////////////////////////////////////////////////////
// Read Data
// Returns:
// ~ r2: The read bit (0 or 1)
//////////////////////////////////////////////////////

Read_Data:
	PUSH 	{lr}							// Start function

	MOV 	r0, #10 						// r0 = 10 (for GPIO10)
	LDR 	r2, =0x3F200000 					// Address for GPFSEL0
	LDR 	r1, [r2, #52] 						// GPLEV0

	MOV 	r3, #1
	LSL 	r3, r0							// Align pin10 bit
	AND 	r1, r3 							// Mask everything else
	TEQ 	r1, #0

	MOVeq 	r2, #0 							// Return 0 if equal
	MOVne 	r2, #1 							// Return 1 if not equal

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
	PUSH 	{r4-r10, lr}						// Start function

  	MOV 	buttons_r, #0 						// Register for sampling buttons
	MOV 	r1, #1
	BL 	Write_Clock						// writeGPIO(CLOCK,#1)
  	MOV 	r1, #1
	BL 	Write_Latch						// writeGPIO(LATCH,#1)
  	MOV 	r0, #12
	BL 	Wait 							// wait(12ms) - signal to SNES to sample buttons
    	MOV 	r1, #0
	BL 	Write_Latch						// writeGPIO(LATCH,#0)

	MOV 	counter_r, #0 						// i = 0

pulseLoop:
	MOV 	r0, #6 							// r0 = 6ms
	BL 	Wait  							// wait(6ms)
	MOV 	r1, #0
	BL 	Write_Clock						// writeGPIO(CLOCK,#0) - falling edge
  	MOV 	r0, #6
	BL 	Wait  							// wait(6ms)

	BL 	Read_Data 						// readGPIO(DATA, b) - read bit i into r2
	CMP 	r2, #0 							// Check if the i-th button is pressed
	Bne 	skip							// If button is not pressed (!= 0) check next bit

	MOV     r2, #1                          			// Else r2 = 1
	LSL     r2, buttons_r	                  			// Adjust to the correct index
	ORR     btnsOld_r, r2                   			// Add 1 to that index to indicate it is not pressed

skip:
	MOV 	r1, #1
	BL 	Write_Clock						// writeGPIO(CLOCK,#1) - rising edge; new cycle

	ADD 	counter_r, #1 						// i++ to look at next button
	CMP 	counter_r, #16						// If counter < 16
	Blt 	pulseLoop 						// Loop, else...

done:
	mov     r0, btnsOld_r						// Return 16 bit button sampling in r0
	POP 	{r4-r10, pc} 						// End function

//////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////

.section 	.data
.align

authors: 		.asciz 	"Created by Marc-Andre Fichtel and Cardin Chen\n\r"
button_press: 		.asciz 	"Please press a button...\n\r"
endMsg: 		.asciz 	"Program is terminating...\n\r"
button_message: 	.asciz 	"You have pressed B              \n\r", "You have pressed Y              \n\r", "You have pressed SELECT         \n\r", "You have pressed START          \n\r", "You have pressed UP             \n\r", "You have pressed DOWN           \n\r", "You have pressed LEFT           \n\r", "You have pressed RIGHT          \n\r", "You have pressed A              \n\r", "You have pressed X              \n\r", "You have pressed LEFT BUMPER    \n\r", "You have pressed RIGHT BUMPER   \n\r" // each string is aligned to 34 bits
