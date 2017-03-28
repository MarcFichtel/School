.section .text

///////////////////////////////////////////////////////////////////////////////////////////////
// Convert an Integer to a String
// Inputs:
// ~ r0: Integer
// ~ r1: Location of output string
///////////////////////////////////////////////////////////////////////////////////////////////

.globl IntegerToString			// Make function global
IntegerToString:
	PUSH	{r4-r7, lr} 		// Start function
	
	MOV	r5, r0			// Move integer to safe register
	MOV	r6, r1			// Move string address to safe register
	MOV	r4, #0			// Counter = 0
		
stringLoop:
	MOV	r0, r5			
	MOV	r1, #10			
	BL	ModularDivision		// ModDiv(Integer, 10)
	LDR	r7, ='0'		// Load the value of '0'
	ADD	r0, r7			// Add the ASCII value of 0 to the result
	STRB	r0, [r6, r4] 		// Store result in given string address	
	ADD	r4, #1			// Increment counter
	MOV	r0, r5
	MOV	r1, #10
	BL	DivideTwoNum		// Div(Integer, 10)
	MOV	r5, r0			// Store new integer value
	CMP	r5, #1			// If greater than or equal to 1
	Bge	stringLoop		// Loop, else...
	CMP	r4, #1			// If string length = 1
	Beq	toStringDone$ 		// Finish function, else...
	MOV	r0, r6			
	BL	FlipString		// Flip the string
toStringDone$:
	POP		{r4-r7, pc} 	// End function

///////////////////////////////////////////////////////////////////////////////////////////////
// Divide one number by another
// Inputs:
// ~ r0: Dividend
// ~ r1: Divisor
// Outputs:
// ~ r0: Result
///////////////////////////////////////////////////////////////////////////////////////////////

.globl DivideTwoNum 			// Make function global
DivideTwoNum:
	PUSH	{r4-r7, lr} 		// Start function
	
	MOV	r5, r0			// Store dividend in safe reg
	MOV	r6, r1			// Store divisor in safe reg
	MOV	r4, #1			// Bit controls division process
t1:
	// Move divisor until greater than dividend	
	CMP	r6, #0x80000000 
	CMPcc	r6, r5
	MOVcc	r6, r6, LSL #1
	MOVcc	r4, r4, LSL #1
	Bcc 	t1
	MOV	r7, #0
t2:
	CMP	r5, r6			// Check if subtraction is possible
	SUBcs	r5, r5, r6		// If so, subtract
	ADDcs	r7, r7, r4		// Insert relevant bit into result
	MOVs	r4, r4, LSR #1		// Shift control bit
	MOVne	r6, r6, LSR #1		// Divide by two if not done
	Bne	t2			// Loop, else
	MOV	r0, r7			// Return result
	
	POP	{r4-r7, pc} 		// End function
	
///////////////////////////////////////////////////////////////////////////////////////////////
// Perform a Modular Division
// Inputs:
// ~ r0: Dividend
// ~ r1: Divisor
// Outputs:
// ~ r0: Result
///////////////////////////////////////////////////////////////////////////////////////////////

.globl ModularDivision 			// Make function global
ModularDivision:
	PUSH	{r4-r7, lr} 		// Start function
	
	MOV	r4, r0			// Store dividend in safe reg
	MOV	r5, r1			// Store divisor in safe reg
	MOV	r0, r4			
	MOV	r1, r5			
	BL	DivideTwoNum		// Div(dividend, divisor)
	MOV	r6, r0			// Move temp1 (result of division) into safe reg
	MUL	r0, r6, r5		// temp2 = temp1 * divisor 
	SUB	r7, r4, r0		// Get final result
	MOV	r0, r7			// Return result
	
	POP	{r4-r7, pc}		// End function

///////////////////////////////////////////////////////////////////////////////////////////////
// Flip a string
// Inputs:
// ~ r0: Address of a string
///////////////////////////////////////////////////////////////////////////////////////////////

.globl FlipString 			// Make function global
FlipString:
	PUSH	{r4-r7, lr} 		// Start function

	MOV	r4, #0			// Counter = 0
	MOV	r5, r0			// Move string address to safe reg
	MOV	r0, r5			
	BL	StringLength		// StringLength(string)
	MOV	r7, r0			// Move string length to safe reg
	SUB	r7, #1			// Decrement length
flip:	
	LDRB	r6, [r5, r4] 		// Load next old character
	LDRB	r0, [r5, r7] 		// Load next new character
	STRB	r0, [r5, r4] 		// Store new character
	STRB	r6, [r5, r7]		// Store old character
	ADD	r4, #1			// Counter++
	SUB	r7, #1			// Decrement length
	CMP	r4, r7			// Compare counter against length
	Blo	flip 			// Loop if switch is not complete yet

	POP	{r4-r7, pc}	 	// End function
	
///////////////////////////////////////////////////////////////////////////////////////////////
// Count a string's length
// Inputs:
// ~ r0: Address of a string
// Outputs:
// ~ r0: String length
///////////////////////////////////////////////////////////////////////////////////////////////

.globl StringLength 			// Make function global
StringLength:
	PUSH	{r4-r6, lr} 		// Start function

	LDRB	r6, [r5]		// Load first character
lengthLoop$:	
	ADD	r4, #1			// Counter++
	LDRB	r6, [r5, r4]		// Load next character
	CMP	r6, #0			// Check if EOS reached
	Bne	lengthLoop$ 		// If not, loop to nect character
	MOV	r0, r4			// Else, return counter (string length)
	
	pop	{r4-r6, pc}		// End function
