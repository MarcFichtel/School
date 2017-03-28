.section .text

///////////////////////////////////////////////////////////////////////////////////////////////
// Draw a 64x64 square
// Inputs:
// ~ r0: X
// ~ r1: Y
// ~ r2: Color
///////////////////////////////////////////////////////////////////////////////////////////////

.globl DrawSquare 			// Make function global
DrawSquare:
	PUSH 	{r4-r8, lr} 		// Start function
	
	// Setup
	MOV	r4, r0			
	MOV	r5, r1		
	MOV	r6, r2			
	MOV	r7, #0		
	MOV	r8, #0			
square:
	// DrawPixel(X, Y, Color)	
	MOV	r0, r7		
	ADD	r0, r4			 
	MOV	r1, r8			
	ADD	r1, r5			
	MOV	r2, r6			
	BL	DrawPixel		
	
	ADD 	r7, #1			// X++
	CMP	r7, #64			// Draw up to X pixel 64
	Ble	square 			// Loop row
	MOV	r7, #0			// Reset X
	ADD 	r8, #1			// Y++
	CMP	r8, #64			// Draw up to Y pixel 64
	Ble	square 			// Loop column
		
	POP 	{r4-r8, pc} 		// End function

///////////////////////////////////////////////////////////////////////////////////////////////
// Draw a vertical line
// Inputs:
// ~ r0: X
// ~ r1: Y
// ~ r2: Y boundary
// ~ r3: Color
///////////////////////////////////////////////////////////////////////////////////////////////

.globl DrawLineY 			// Make function global
DrawVertLine:
	PUSH 	{r4-r7, lr}		// Start function
	
	// Setup 
	MOV	r4, r0
	MOV	r5, r1
	MOV	r7, r2
	MOV	r6, r3
vertLine:	
	// DrawPixel(X, Y, Color)
	MOV	r0, r4
	MOV	r1, r5
	MOV	r2, r7
	BL	DrawPixel
	
	ADD	r5, #1			// Y++
	CMP	r5, r6			// Check if boundary reached
	Bne	vertLine		// If not, loop

	POP  	{r4-r7, pc} 		// End function

///////////////////////////////////////////////////////////////////////////////////////////////
// Draw a horizontal line
// Inputs:
// ~ r0: X
// ~ r1: Y
// ~ r2: X boundary
// ~ r3: Color
///////////////////////////////////////////////////////////////////////////////////////////////

.globl DrawLineX 			// Make function global
DrawHorizLine:
	PUSH 	{r4-r7, lr} 		// Start function
	
	// Setup
	MOV	r4, r0
	MOV	r5, r1
	MOV	r7, r2
	MOV	r6, r3
horizLine:	
	// DrawPixel(X, Y, Color)
	MOV	r0, r4
	MOV	r1, r5
	MOV	r2, r7
	BL	DrawPixel		
	
	ADD	r4, #1			// X++
	CMP	r4, r6			// Check if boundary reached
	Bne	horizLine 		// If not, loop

	POP  {r4-r7, pc} 		// End function
