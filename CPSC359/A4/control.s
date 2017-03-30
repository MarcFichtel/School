////////////////////////////////////////////////////////////////////////////////
// SNES controller stuff
////////////////////////////////////////////////////////////////////////////////

.section .text

////////////////////////////////////////////////////////////////////////////////
// Initialize SNES controller
////////////////////////////////////////////////////////////////////////////////

.globl InitSNES 			// Make function global
InitSNES:
	PUSH	{r4-r5, lr} 		// Start function

	// Set GPIO11 to output (Clock)	
	LDR	r4, =0x3F200004		// Load GPFSEL1 address
	LDR	r0, [r4]		// Load GPFSEL1 value
	MOV	r1, #7			// Set up mask b0111
	LSL	r1, #3			// Align to clear bits 3-5
	BIC	r0, r1			// Clear bits 3-5
	MOV	r2, #1			// Output = 001 
	LSL	r2, #3			// Align output code to line 1 (GPIO 11)
	ORR	r0, r2			// Set to output
	STR	r0, [r4]		// Write to GPFSEL1

	// Set GPIO9 to output (Latch)
	LDR	r5, =0x3F200000		// Load GPFSEL0 address
	LDR	r0, [r5]		// Load GPFSEL0 value
	MOV	r1, #7			// Set up mask b0111
	LSL	r1, #27			// Align to clear bits 27-29
	BIC	r0, r1			// Clear bits 27-29
	MOV	r2, #1			// Output = 001 
	LSL	r2, #27			// Align output code to line 9 (GPIO 9)
	ORR	r0, r2			// Set to output
	STR	r0, [r5]		// Write to GPFSEL0
	
	// Set GPIO10 to input (Data)
	LDR	r0, [r4]		// Load GPFSEL0 value
	MOV	r1, #7			// Set up mask b0111
	BIC	r0, r1			// Clear bits on line 1 (output = 000)
	STR	r0, [r4]		// Write 1 (input) to GPFSEL0
	
	POP	{r4-r5, pc} 		// End function

////////////////////////////////////////////////////////////////////////////////
// Read Data line
// Outputs:
// ~ r0: Result of read bit {0, 1}
////////////////////////////////////////////////////////////////////////////////
	
.globl ReadData 			// Make function gobal
ReadData: 
	PUSH	{r4-r5, lr}		// Start function
	
	LDR	r4, =0x3F200000		// Load GPFSEL0 address	
	MOV	r1, #10			// Data = GPIO10
	LDR	r3, [r4, #52]		// GPLEV0
	MOV	r2, #1			// Prepare mask
	LSL	r2, r1			// Align to GPIO10
	AND	r3, r2			// Mask GPLEV0
	TEQ	r3, #0			// Check if pin is low
	MOVeq	r0, #0			// Return 0 in r0 if low
	MOVne	r0, #1			// Return 1 in r0 if high
	
	POP	{r4-r5, pc}		// End function
	
////////////////////////////////////////////////////////////////////////////////
// Write to Clock line
// Inputs:
// ~ r0: Value to write {0, 1}
////////////////////////////////////////////////////////////////////////////////	

.globl WriteClock 			// Make function global
WriteClock:
	PUSH	{r4, lr} 		// Start function
	
	LDR	r4, =0x3F200000		// Load GPFSEL0 address	
	MOV	r1, #1			// Prepare mask
	MOV	r2, #11			// Clock = GPIO11
	LSL	r1, r2			// Align to GPIO11
	TEQ	r0, #0			// Check if input is 0
	STReq	r1, [r4, #40]		// GPSCLR0 if input is 0
	STRne 	r1, [r4, #28]		// GPSET0 if input is 1
	
	POP	{r4, pc} 		// End function
	
////////////////////////////////////////////////////////////////////////////////
// Write to Latch line
// Inputs:
// ~ r0: Value to write {0, 1}
////////////////////////////////////////////////////////////////////////////////

.globl	WriteLatch 			// Make function gobal
WriteLatch:
	PUSH	{r4, lr} 		// Start function

	LDR	r4, =0x3F200000		// Load GPFSEL0 address	
	MOV	r1, #1			// Prepare mask
	MOV	r2, #9			// Latch = GPIO9 
	LSL	r1, r2			// Align to GPIO9
	TEQ	r0, #0			// Check if input is 0
	STReq	r1, [r4, #40]		// GPSCLR0 if input is 0
	STRne 	r1, [r4, #28]		// GPSET0 if input is 1
	
	POP	{r4, pc}		// End function

////////////////////////////////////////////////////////////////////////////////
// Read SNES Controller
// Outputs:
// ~ r0: SNES controller button sampling
////////////////////////////////////////////////////////////////////////////////
 
.globl ReadSNES 			// Make function global
ReadSNES:
	PUSH	{r4-r5, lr} 		// Start function
	
	MOV	r5, #0			// r5 = 0
	MOV	r0, #1		
	BL	WriteClock		// WriteClock(1)
	MOV	r0, #1		
	BL	WriteLatch		// WriteLatch(1)
	MOV	r0, #12		
	BL	Wait			// Wait(12)
	BL	WriteLatch		// WriteLatch(0)
	MOV	r4, #0			// r4 = counter = 0

pulseloop:
	MOV	r0, #6	
	BL	Wait			// Wait(6)
	BL	WriteClock		// WriteClock(0)
	
	MOV	r0, #6	
	BL	Wait			// Wait(6)
	BL	ReadData		// ReadData()
	CMP	r0, #1			// Check read data
	Bne	continue		// Exit loop when read bit is low, else button is pressed
	MOV	r2, #1			// Prepare mask
	LSL	r2, r4			// Align mask to current button
	ORR	r5, r2			// Write the current button press

continue:
	MOV	r0, #1	
	BL	WriteClock 		// WriteClock(1)
	ADD	r4, #1			// Increment counter
	CMP	r4, #16			// Check if counter = 16
	Bne	pulseloop		// If counter < 16, read next button
	MOV	r0, r5			// Else return buton sampling

	POP	{r4-r5, pc} 		// End function

////////////////////////////////////////////////////////////////////////////////
// Wait
// Inputs:
// ~ r0: Time to wait in microseconds
////////////////////////////////////////////////////////////////////////////////
 
.globl	Wait 				// Make function global
Wait:
	PUSH	{r4, lr} 		// Start function

	LDR	r4, =0x3F003004		// Load Clock address
	LDR	r1, [r4]		// Load Clock value
	ADD	r1, r0			// Add <input> ms
	
waitloop:
	LDR	r2, [r4]		// Load Clock value
	CMP	r1, r2			// Stop when Clock = r1 (time to wait until)
	Bhi	waitloop		// Loop
	MOV	r0, #0			// Reset r0
	
	POP	{r4, pc} 		// End function
