//////////////////////////////////////////////////////
// CPSC 359
// Assignment 2
// Students:
// ~ Marc-Andre Fichtel, 30014709
// ~ Cardin Chen, 10161477
//
// TODOs
// * Rewrite print summary to use multiple strings (i.e. one for each statistic), since we can't pass more than 4 arguments
// * Check if &f is the correct placeholder for floats in strings
// * Check if align statements in data section are correct and work as intended
// * Run the thing
//
// Assembly program does the following:
// ~ Draws objects using asterisks
// ~ Calculates the total number of asterisks
// ~ Calculates the mean of asterisks grouped by object
//
// Register usage
// ~  r4 = menu choice
// ~  r5 = width input (for squares and rectangles)
// ~  r6 = height input (for rectangles and triangles)
// ~  r7 = used for various statistics
// ~  r8 = used for various memory addresses of statistics
//////////////////////////////////////////////////////

choice_r 	.req r4
width_r 	.req r5
height_r 	.req r6
stats_r 	.req r7
addr_r 		.req r8

.section 	.init
.global 	_start

_start:
	B 	main

.section 	.text

main:
	// Start program (as seen on lecture slides)
	MOV 	sp, #0x8000 						// Establish stack pointer
	BL 		EnableJTAG 							// Enable JTAG
	BL 		InitUART 							// Enable UART

	// Print names
	LDR 	r0, =authors 						// Load 'author' string into register
	BL 		stringBitCount 						// Branch to count bits & print

start:
	LDR 	r0, =instrct 						// Print instruction
	BL 		stringBitCount 						// Branch to ascii-to-bit counter

	// Get input and move it into into choice_r
	LDR   	addr_r, =buffer 					// Get buffer memory address
	MOV 	r1, #256 							// Load buffer size into r1
	BL 		ReadLineUART 						// Call ReadLineUART to store user input into buffer
	LDR 	choice_r, [addr_r] 					// Load input from buffer into choice_r

	// Validate input
	CMP 	choice_r, #-1 						// If menu choice = -1...
	Beq 	summary 							// Go to summary...
	Blt 	error1 								// Or to error1, if menu choice < -1

	CMP 	choice_r, #1 						// Else if menu choice = 1...
	Beq 	square 								// Go to square

	CMP 	choice_r, #2						// Else if menu choice = 2...
	Beq 	rect 								// Go to rect(angle)

	CMP 	choice_r, #3 						// Else if menu choice = 3...
	Beq 	triangle 							// Go to triangle...
	Blt 	error1 								// Or to error1, if menu choice > 3

	CMP 	choice_r, 0x71 						// Else if menu choice = q (in hex --> 01110001 in binary)...
	Beq 	exit 								// Go to exit
	
	B 		error2 								// Else go to error2 (menu choice was not -1, 1, 2, 3, or q)

error1:
	// Print error1, then return to start menu
	LDR 	r0, =instrctEr1 					// Load 'error1' string into register
	BL 		stringBitCount 						// Branch to count bits & print
	B 		start 								// Return to menu

error2:
	// Print error2, then return to start menu
	LDR 	r0, =instrctEr2 					// Load 'error2' string into register
	BL 		stringBitCount 						// Branch to count bits & print
	B 		start 								// Return to menu

square:
	// Print square width input instruction
	LDR 	r0, =widInstrct 					// Load 'width' string into register
	BL 		stringBitCount 						// Branch to count bits & print

	// Get square width input into width_r
	LDR   	addr_r, =buffer 					// Get buffer memory address
	MOV 	r1, #256 							// Load buffer size into r1
	BL 		ReadLineUART 						// Call ReadLineUART to store user input into buffer
	LDR 	width_r, [addr_r] 					// Load input from buffer into width_r

	// Validate square width input
	CMP 	width_r, #3 						// If width < 3...
	Blt 	error3 								// Go to error3
	CMP 	width_r, #9 						// Else if width > 9...
	Bgt 	error3 								// Go to error3

	// Draw square asterisks
	MOV 	r0, width_r 						// r0 = width_r (width)
	MOV 	r1, width_r 						// r1 = width_r (width)
	B 		drawTest1 							// Go to drawTest1

draw11:
	SUB 	r0, r0, #1 							// r0--
	MOV 	r1, width_r 						// r1 = width_r again
	LDR 	r0, =starEnd 						// Load 'starEnd' string into register
	BL 		stringBitCount 						// Branch to count bits & print
	B 		drawTest1 							// Go to drawTest1

draw12:
	SUB 	r1, r1, #1 							// r1--
	LDR 	r0, =star							// Load 'star' string into register
	BL 		stringBitCount 						// Branch to count bits & print

drawTest1:
	CMP 	r0, #1 								// If r0 < 1...
	B.LT 	next1 								// Go to draw11
	CMP 	r1, #1 								// If r1 > 1...
	B.GT 	draw12 								// Go to draw12
	B 		draw11 								// Else go to draw11

next1:
	// Store square info for summary computation
	LDR 	addr_r, =numSquares 				// Get numSquares address
	LDR 	stats_r, [addr_r] 					// Load number of squares into stats_r
	ADD 	stats_r, stats_r, #1 				// Increment # of squares
	STR 	stats_r, [addr_r] 					// Store incremented number of squares back into memory

	LDR 	addr_r, =numSquareStars				// get numSquareStars address
	LDR 	stats_r, [addr_r]					// load number of square stars into stats_r
	LSL 	width_r, width_r, #1 				// width_r = width_r * 2^1
	ADD 	stats_r, stats_r, width_r 			// Add new square asterisks to # of square asterisks
	STR 	stats_r, [addr_r] 					// Store new number of square stars back into memory

	B 		start 								// Return to menu

rect:
	// Print rectangle width input instruction
	LDR 	r0, =widInstrct 					// Load 'width' string into register
	BL 		stringBitCount 						// Branch to count bits & print

	// Get rectangle width input into width_r
	LDR   	addr_r, =buffer 					// Get buffer memory address
	MOV 	r1, #256 							// Load buffer size into r1
	BL 		ReadLineUART 						// Call ReadLineUART to store user input into buffer
	LDR 	width_r, [addr_r] 					// Load input from buffer into width_r

	// Validate rectangle width input
	CMP 	width_r, #3 						// If width < 3...
	B.LT 	error3 								// Go to error3
	CMP 	width_r, #9 						// Else if width > 9...
	B.GT 	error3 								// Go to error3

	// Print rectangle height input instruction
	LDR 	r0, =higInstrct 					// Load 'height' string into register
	BL 		stringBitCount 						// Branch to count bits & print

	// Get rectangle height input into height_r
	LDR   	addr_r, =buffer 					// Get buffer memory address
	MOV 	r1, #256 							// Load buffer size into r1
	BL 		ReadLineUART 						// Call ReadLineUART to store user input into buffer
	LDR 	height_r, [addr_r] 					// Load input from buffer into height_r

	// Validate rectangle height input
	CMP 	height_r, #3 						// If height < 3...
	B.LT 	error3 								// Go to error3
	CMP 	height_r, #9 						// Else if height > 9...
	B.GT 	error3 								// Go to error3

	// Draw rectangle asterisks
	MOV 	r0, width_r 						// r0 = width_r (width)
	MOV 	r1, height_r 						// r1 = height_r (height)
	B 		drawTest2 							// Go to drawTest2

draw21:
	SUB 	r0, r0, #1 							// r0--
	MOV 	r1, height_r 						// r1 = height_r again
	LDR 	r0, =starEnd 						// Load 'starEnd' string into register
	BL 		stringBitCount 						// Branch to count bits & print
	B 		drawTest2 							// Go to drawTest2

draw22:
	SUB 	r1, r1, #1 							// r1--
	LDR 	r0, =star							// Load 'star' string into register
	BL 		stringBitCount 						// Branch to count bits & print

drawTest2:
	CMP 	r0, #1 								// If r0 < 1...
	B.LT 	next2 								// Go to next2
	CMP 	r1, #1 								// If r1 > 1...
	B.GT 	draw22 								// Go to draw22
	B 	draw21 									// Else go to draw21

next2:
	// Store rectangle asterisks for summary computation
	LDR 	addr_r, =numRects 					// Get numRects address
	LDR 	stats_r, [addr_r] 					// Load numRects into stats_r
	ADD 	stats_r, stats_r, #1 				// Increment # of rectangles
	STR 	stats_r, [addr_r] 					// Store incremented numRects back into memory
	
	LDR 	addr_r, =NumRectStars 				// Get numRectStars address
	LDR 	stats_r, [addr_r] 					// Load numRectStars into stats_r
	MLA 	stats_r, width_r, height_r, stats_r	// stats_r += width_r * height_r
	STR 	stats_r, [addr_r] 					// Store new numRectStars back into memory

	B 	start 									// Return to menu

triangle:
	// Print triangle height input instruction
	LDR 	r0, =higInstrct 					// Load 'height' string into register
	BL 	stringBitCount 							// Branch to count bits & print

	// Get triangle height input (TODO Check: Is this how we measure triangles???)
	LDR   	addr_r, =buffer 					// Get buffer memory address
	MOV 	r1, #256 							// Load buffer size into r1
	BL 	ReadLineUART 							// Call ReadLineUART to store user input into buffer
	LDR 	height_r, [addr_r] 					// Load input from buffer into height_r

	// Validate triangle height input
	CMP 	height_r, #3 						// If height < 3...
	B.LT 	error3 								// Go to error3
	CMP 	height_r, #9 						// Else if height > 9...
	B.GT 	error3 								// Go to error3

	// Draw triangle asterisks
	MOV 	r0, height_r 						// r0 = width_r (height)
	MOV 	r1, height_r 						// r1 = height_r (height)
	B 	drawTest3 								// Go to drawTest3

draw31:
	SUB 	r0, r0, #1 							// r0--
	MOV 	r1, r0 								// r1 = r0
	LDR 	r0, =starEnd 						// Load 'starEnd' string into register
	BL 		stringBitCount 						// Branch to count bits & print
	B 	drawTest2 								// Go to drawTest3

draw32:
	SUB 	r1, r1, #1 							// r1--
	LDR 	r0, =star							// Load 'star' string into register
	BL 		stringBitCount 						// Branch to count bits & print

drawTest3:
	CMP 	r0, #1 								// If r0 < 1...
	B.LT 	next3 								// Go to next3
	CMP 	r1, #1 								// If r1 > 1...
	B.GT 	draw32 								// Go to draw32
	B 		draw31 								// Else go to draw31

next3:
	// Store triangle asterisks for summary computation
	LDR 	addr_r, =numTris 					// Get numTris address
	LDR 	stats_r, [addr_r] 					// Load numTris into stats_r
	ADD 	stats_r, stats_r, #1 				// Increment # of triangles
	STR 	stats_r, [addr_r] 					// Store incremented numTris back into memory

	LDR 	addr_r, =numTriStars 				// Get numTriStars address
	LDR 	stats_r, [addr_r] 					// load numTriStars into stats_r
loop:											// # of triangle asterisks = factorial(height_r):
	ADD 	stats_r, stats_r, height_r 			// # of triangle asterisks += height
	SUB 	height_r, height_r #1 				// height--
	CMP 	height_r, #1						// If height >= 1...
	B.GE 	loop 								// Go to loop

	ADD 	stats_r, stats_r, r0 				// Add new triangle asterisks to # of triangle asterisks
	STR 	stats_r, [addr_r] 					// Store new numTriStars back into memory
	B 		start 								// Return to menu

	// Print error3, then return to start menu
error3:
	LDR 	r0, =dimError 						// Load 'error3' string into register
	BL 		stringBitCount 						// Branch to count bits & print
	B 		start 								// Return to menu

summary:
	// Compute total number of asterisks into r1 (first arg)
	LDR 	addr_r, =numSquareStars 			// Get numSquareStars address
	LDR 	stats_r, [addr_r]					// Load numSquareStars into stats_r
	MOV 	r1, stats_r 						// r1 = stats_r (# square asterisks)
	LDR 	addr_r, =numRectStars				// Get numRectStars address
	LDR 	stats_r, [addr_r]					// Load numRectStars into stats_r
	ADD 	r1, r1, stats_r 					// r1 += stats_r (# rectangle asterisks)
	LDR 	addr_r, =numTriStars				// Get numTriStars address
	LDR 	stats_r, [addr_r]					// Load numTriStars into stats_r
	ADD 	r1, r1, stats_r 					// r1 += stats_r (# trianle asterisks)

	// Compute mean of square asterisks (second arg)
	LDR 	addr_r, =numSquareStars 			// Get numSquareStars address
	LDR 	r2, [addr_r]						// Load numSquareStars into r2
	LDR 	addr_r, =numSquares 				// Get numSquares address
	LDR 	stats_r, [addr_r]					// Load numSquares into stats_r
	DIV 	r2, r2, stats_r 					// r2 /= stats_r

	// Compute mean of rectangle asterisks (third arg)
	LDR 	addr_r, =numRectStars 				// Get numRectStars address
	LDR 	r3, [addr_r]						// Load numRectStars into r3
	LDR 	addr_r, =numRects 					// Get numRects address
	LDR 	stats_r, [addr_r]					// Load numRects into stats_r
	DIV 	r3, r3, stats_r 					// r3 /= stats_r

	// Compute mean of triangle asterisks (fourth arg)
	// TODO Check if choice_r can be used to pass an arg for printing
	LDR 	addr_r, =numTriStars 				// Get numTriStars address
	LDR 	choice_r, [addr_r]					// Load numTriStars into choice_r
	LDR 	addr_r, =numTris 					// Get numTris address
	LDR 	stats_r, [addr_r]					// Load numTris into stats_r
	DIV 	choice_r, choice_r, stats_r 		// choice_r /= stats_r

	// Display summary, then return to start menu
	LDR 	r0, =summ 							// Load 'summary' string into register
	BL 		stringBitCount 						// Branch to count bits & print
	B 		start 								// Return to menu

	// Count number of bits to buffer for any given particular string
stringBitCount: 
	PUSH 	{lr}								// Start function (push link reg)
	MOV 	r1, #0 								// Reset loop counter
	MOV 	r2, r0 								// Move print address into temporary register

loopCounter:
	LDRB 	r3, [r2], #1 						// Load byte into memory
	CMP 	r3, #0 								// Compare current byte to null
	ADDne 	r1, #1  							// If not equal, loop++, and
	Bne 	loopCounter 						// Loop
	BL 		WriteStringUART 					// Else, when done counting, print out r1 amount of buffer
	POP 	{pc} 								// End function (return to calling code by popping program counter)

exit:
	// Print exit message, then exit program
	LDR 	r0, =fmtDone 						// Load 'exit' string into register
	BL 		stringBitCount 						// Branch to count bits & print

haltLoop$:
	B 		haltLoop$ 							// Infinite loop at end ensures cmd prompt remains open after termination

.section 	.data
.align 2 										// Align to doubleword boundary for strings (???)
authors: 		.asciz 	"Created by Marc-Andre Fichtel and Cardin Chen\n"
instrct: 		.asciz 	"Please enter the number of objects you want to draw.\nPress -1 for Summary or q to exit\n1: Square --- 2: Rectangle --- 3: Triangle\n"
instrctEr1: 	.asciz 	"Invalid choice, input must be between 1 and 3, or -1 for Summary\n"
instrctEr2: 	.asciz 	"Wrong format, only q is allowed to exit the program\n"
widInstrct: 	.asciz 	"Please enter width (must be between 3 and 9)\n"
higInstrct: 	.asciz 	"Please enter height (must be between 3 and 9)\n"
dimError: 		.asciz 	"Invalid choice, input must be between 3 and 9\n"
star: 			.asciz 	"* "
starEnd: 		.asciz 	"*\n"
summ: 			.asciz 	"Total number of stars: %d\nMean of stars used to draw squares: %f\nMean of stars used to draw rectangles: %f\nMean of stars used to draw triangles: %f\n"
fmtDone: 		.asciz 	"Exiting program...\n"

numSquares: 	.int 	0
numSquareStars: .int	0
numRects:		.int 	0
numRectStars:	.int	0
numTris: 		.int	0
numTriStars:	.int 	0

buffer:			.rept 	256
				.byte 	0
				.endr	
