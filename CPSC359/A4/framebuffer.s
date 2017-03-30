//////////////////////////////////////////////////////////////////////////////////////
// FrameBuffer stuff
//////////////////////////////////////////////////////////////////////////////////////

.section .text

//////////////////////////////////////////////////////////////////////////////////////
// Initialize the FrameBuffer using the FrameBufferInit structure
// Code taken from tut08 example
// Returns:
// ~ r0: 0 on failure, framebuffer pointer on success
//////////////////////////////////////////////////////////////////////////////////////

.globl InitFrameBuffer 			  // Make function global

InitFrameBuffer:
	LDR	r2, =0x3F00B880           // Get address of mailbox0
	LDR	r3, =FrameBufferInit      // Get address of framebuffer init structure

mBoxFullLoop$:
	LDR	r0, [r2, #0x18]           // Load value of mailbox status register
	TST	r0, #0x80000000           // Loop while bit 31 (Full) is set
	Bne	mBoxFullLoop$

	ADD	r0, r3,	#0x40000000       // Add 0x40000000 to address of fbinit struct, store in r0
	ORR	r0, #0b1000               // OR with framebuffer channel (1)
	STR	r0, [r2, #0x20]           // Write value to mailbox write register

mBoxEmptyLoop$:
	LDR	r0, [r2, #0x18]           // Load value of mailbox status register
	TST	r0, #0x40000000           // Loop while bit 30 (Empty) is set
	Bne	mBoxEmptyLoop$

	LDR	r0, [r2, #0x00]           // Read response from mailbox read register
	AND	r1, r0, #0xF              // AND-mask out channel information (lowest 4 bits)
	TEQ	r1, #0b1000               // Test if message is for framebuffer channel (1)
	Bne	mBoxEmptyLoop$            // If not, read another message from the mailbox

	LDR	r0, =FrameBufferInit
	LDR	r1, [r0, #0x04]	          // Load request/ response word from buffer
	TEQ	r1, #0x80000000	          // Test if request was successful
	Beq	pointerWaitLoop$
	MOVne	r0, #0		          // Return 0 if request failed
	BXne	lr

pointerWaitLoop$:
	LDR	r0, =FrameBuffer
	LDR	r0, [r0]
	TEQ	r0, #0	                  // Test if framebuffer pointer has been set
	Beq	pointerWaitLoop$

	LDR 	r3, =FrameBufferPointer
	STR	r0, [r3]
	BX	lr

//////////////////////////////////////////////////////////////////////
// Clear Screen (paints display black)
/////////////////////////////////////////////////////////////////////

.globl ClearScreen		  	// Make function global
ClearScreen:

	PUSH 	{r4-r6, lr}		// Start function

	MOV 	r4, #0 			// X = 0
	MOV 	r5, #0 			// Y = 0
	MOV 	r6, #0 			// Color = Black
clear:
	MOV 	r0, r4
	MOV 	r1, r5
	MOV 	r2, r6
	BL 	DrawPixel 		// DrawPixel (X, Y, #white)

	ADD 	r4, #1 			// X++
	CMP 	r4, #1024 		// If X < 1024
	Blt 	clear 			// Loop

	MOV 	r4, #0 			// X = 0
	ADD 	r5, #1 			// Y++
	CMP 	r5, #768 		// If Y < 768
	Blt 	clear 			// Loop

	POP 	{r4-r6, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw a Square
// Inputs:
// ~ r0: Starting point X
// ~ r1: Starting point Y
// ~ r2: Square Size
// ~ r3: Color
/////////////////////////////////////////////////////////////////////

.globl DrawSquare		  	// Make function global
DrawSquare:
	PUSH 	{r4-r9, lr}		// Start function

	// Name a few registers
	pointX 	.req r4
	pointY 	.req r5
	color 	.req r6
	size 	.req r7
	borderX .req r8
	borderY .req r9

	MOV 	pointX, r0 		// X = r0
	MOV 	pointY, r1 		// Y = r1
	MOV 	color, r3 		// Color = r3
	MOV 	size, r2 		// Size = r2

	MOV 	borderX, size
	ADD 	borderX, pointX		// borderX = X + size
	MOV 	borderY, size
	ADD 	borderY, pointY		// borderY = Y + size

sqr:
	MOV 	r0, pointX
	MOV 	r1, pointY
	MOV 	r2, color
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size
	Blt 	sqr 			// Loop

	SUB 	pointX, size 		// X -= size
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border
	Blt 	sqr 			// Loop

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq color
	.unreq size
	.unreq borderX
	.unreq borderY

	POP 	{r4-r9, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw Pixel
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Color
/////////////////////////////////////////////////////////////////////

.globl DrawPixel 		  	// Make function global
DrawPixel:
	PUSH	{r4, lr} 		// Start function

	ADD	r4, r0, r1, lsl #10 	// r4 = (y * 1024) + x = x + (y << 10)
	LSL	r4, #1 			// r4 *= 2 (for 16 bits per pixel = 2 bytes per pixel)
	LDR	r0, =FrameBufferPointer	// Get frame buffer pointer address
	LDR	r0, [r0] 		// Load frame buffer pointer into r0
	STRH	r2, [r0, r4] 		// Store color (hword) at framebuffer pointer + offset

	POP	{r4, pc}		// End function

//////////////////////////////////////////////////////////////////////
// Draw a Character (in white)
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Character
/////////////////////////////////////////////////////////////////////

.globl DrawChar		  		// Make function global
DrawChar:
	PUSH	{r4-r10, lr}		// Start function

	// Name a few registers
	charAddr .req	r4
	pixel_x	 .req	r5
	pixel_y	 .req	r6
	row	 .req	r7
	mask	 .req	r8
	char	 .req 	r9
	temp 	 .req 	r10

	LDR	charAddr, =font		// Load address of font map
	MOV	pixel_x, r0		// Move x position to safe reg
	MOV 	temp, r0 		// Move x position to temporary reg
	MOV	pixel_y, r1		// Move y position to safe reg
	MOV 	char, r2 		// Move char to safe reg
	ADD	charAddr, char, LSL #4	// Char address = font base + (char * 16)

charLoop$:
	MOV	pixel_x, temp		// Restore X to initial value
	MOV	mask,	#0x01		// Set bitmask to 1 in the LSB
	LDRB	row, [charAddr], #1	// Load row byte, post-increment charAddr

rowLoop$:
	TST	row, mask		// Test row byte against bitmask
	Beq	noPixel$ 		// If equal, go to noPixel$, else...

	MOV	r0, pixel_x		// 1st arg: X coord
	MOV	r1, pixel_y 		// 2st arg: Y coord
	MOV	r2, #0xFFFFFFFF		// 3rd arg: Color (white)
	BL	DrawPixel		// DrawPixel(X, Y, white)

noPixel$:
	ADD	pixel_x, #1		// Increment X coord
	LSL	mask, #1		// Shift bitmask left by 1

	TST	mask, #0x100		// Test if bitmask has shifted 8 times (i.e. test 9th bit)
	Beq	rowLoop$ 		// If it has shifted 8 times, loop to rowLoop, else...
	ADD	pixel_y, #1		// Increment Y coordinate

	TST	charAddr, #0xF 		// Test if address is evenly divisible by 16
	Bne	charLoop$ 		// If so, loop to charLoop$ (ie: the next char)

	// Unname a few registers
	.unreq	charAddr
	.unreq	pixel_x
	.unreq	pixel_y
	.unreq	row
	.unreq	mask
	.unreq 	char
	.unreq 	temp

	POP	{r4-r10, pc} 		// Else, end function

//////////////////////////////////////////////////////////////////////
// Draw a String (in white)
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: String Address
/////////////////////////////////////////////////////////////////////

.globl DrawString		  	// Make function global
DrawString:
	PUSH	{r5-r7, lr} 		// Start function

	pointX	.req r5 		// Name registers
	pointY	.req r6
	strAddr .req r7

	MOV 	pointX, r0 		// Move input to safe registers
	MOV 	pointY, r1
	MOV 	strAddr, r2

loop1:
	LDRB 	r2, [strAddr], #1 	// Load byte into r3
	CMP 	r2, #0 			// Compare current byte to null

	Beq 	done1 			// If equal, string is done
	BL 	DrawChar		// Else, DrawChar(X, Y, current_character)
	ADD 	pointX, #20		// Offset next character
	MOV 	r0, pointX
	MOV 	r1, pointY
	B 	loop1 			// Then loop to next character

done1:
	.unreq 	pointX 			// Unname registers
	.unreq 	pointY
	.unreq 	strAddr

	POP	{r5-r7, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw an Image (each image is 64x64)
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Image Address
/////////////////////////////////////////////////////////////////////

.globl DrawImage		  	// Make function global
DrawImage:
	PUSH 	{r4-r9, lr} 		// Start function

	// Name a few registers
	pointX 	.req r4
	pointY 	.req r5
	img 	.req r6
	size 	.req r7
	borderX .req r8
	borderY .req r9

	MOV 	pointX, r0 		// X = r0
	MOV 	pointY, r1 		// Y = r1
	MOV 	img, r2 		// img = r3
	MOV 	size, #64 		// All images are 64x64

	MOV 	borderX, size
	ADD 	borderX, pointX 	// borderX = X + size
	MOV 	borderY, size
	ADD 	borderY, pointY 	// borderY = Y + size

imgLoop:
	MOV 	r0, pointX
	MOV 	r1, pointY
	LDRH 	r2, [img], #2
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size
	Blt 	imgLoop 		// Loop

	SUB 	pointX, size 		// X -= 64
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border
	Blt 	imgLoop 		// Loop

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq img
	.unreq size
	.unreq borderX
	.unreq borderY

	POP 	{r4-r9, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw the BG
/////////////////////////////////////////////////////////////////////

.globl DrawBG 				// Make function global
DrawBG:
	PUSH 	{r4-r8, lr} 		// Start function

	// Name a few registers
	pointX 	.req r4
	pointY 	.req r5
	img 	.req r6
	borderX .req r7
	borderY .req r8

	MOV 	pointX, #0 		// X = r0
	MOV 	pointY, #0 		// Y = r1
	LDR 	img, =startmenu 	// img = r3
	MOV 	borderX, #1024 		// borderX = 1024
	MOV 	borderY, #768 		// borderY = 768

bg:
	MOV 	r0, pointX
	MOV 	r1, pointY
	LDRH 	r2, [img], #2
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size, loop, else...
	Blt 	bg

	SUB 	pointX, #1024 		// X -= 40
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border, loop, else...
	Blt 	bg

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq img
	.unreq borderX
	.unreq borderY

	POP 	{r4-r8, pc} 		// End function


//////////////////////////////////////////////////////////////////////
// Draw start menu
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Image Address
/////////////////////////////////////////////////////////////////////

.globl DrawStartMenu		  	// Make function global
DrawStartMenu:
	PUSH 	{r4-r8, lr} 		// Start function

// Name a few registers
	pointX 	.req r4
	pointY 	.req r5
	img 	.req r6
	borderX .req r7
	borderY .req r8

	MOV 	pointX, r0 		// X = r0
	MOV 	pointY, r1 		// Y = r1
	MOV 	img, r2 		// img = r3

	MOV 	borderX, #672 		// position on screen (352) + size  (320)
	MOV 	borderY, #660		// pos on screen (500) + size (160)

imgLoopStart:
	MOV 	r0, pointX
	MOV 	r1, pointY
	LDRH 	r2, [img], #2
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size
	Blt 	imgLoopStart 		// Loop

	SUB 	pointX, #320 		// X -= 320
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border
	Blt 	imgLoopStart 		// Loop

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq img
	.unreq borderX
	.unreq borderY

	POP 	{r4-r8, pc} 		// End function

////////////////////////////////////////////////////////////////////////////////////
// Draw main menu options
////////////////////////////////////////////////////////////////////////////////////

.globl DrawMenu 			// Make function global
DrawMenu:
	PUSH 	{lr}			// Start function

	BL 	DrawBG 			// Draw Background

	MOV 	r0, #352
	MOV 	r1, #500
	LDR 	r2, =menuPointerSTART
	BL 	DrawStartMenu 		// Draw Start String

	MOV 	r0, #20
	MOV 	r1, #700
	LDR 	r2, =authorsString
	BL 	DrawString

	POP 	{pc} 			// End function

////////////////////////////////////////////////////////////////////////////////////
// Draw game scene
////////////////////////////////////////////////////////////////////////////////////

.globl DrawScene
DrawScene:
	PUSH 	{lr}			// Start function

 	BL 	DrawBG 			// Draw the background
	BL	DrawGround  		// Draw the ground
 	BL 	DrawPC 			// Draw the player character

	POP 	{pc} 			// End function

////////////////////////////////////////////////////////////////////////////////////
// Draw ground
////////////////////////////////////////////////////////////////////////////////////

.globl DrawGround
DrawGround:
	PUSH 	{r4, lr}		// Start function

	MOV 	r4, #0 			// X = 0
floorLoop:
	MOV 	r0, r4
	MOV 	r1, #960
	LDR 	r2, =floor 		
	BL 	DrawImage 		// DrawImage(X, 960, floor image addr)

	ADD 	r4, #64 		// X += 64 for next floor tile
	CMP	r4, #768 		// If we is space for another floor tile
	Blt 	floorLoop 		// Draw it, else done

	POP 	{r4, pc} 		// End function

////////////////////////////////////////////////////////////////////////////////////
// Draw player character
////////////////////////////////////////////////////////////////////////////////////

.globl DrawPC
DrawPC:
	PUSH 	{r4, lr}		// Start function

	stateR 	.req r4

	LDR 	stateR, =state 		// Load state address
	LDR 	r2, [stateR, #12] 	// Load PC anim state
	CMP 	r2, #0 			// If anim state = 0	
	LDReq 	r2, =luigi1 		// Load 1st img address
	Beq donePC 			// Branch to donePC
	
	CMP	r2, #1	 		// Else if anim state = 1
	LDReq 	r2, =luigi2 		// Load 2nd img address	
	Beq 	donePC 			// Branch to donePC

	LDR 	r2, =luigijump		// Else load 3rd img address (anim state = 2)
donePC:
	LDR 	r0, [stateR, #4] 	// Load PC position X
	LDR 	r1, [stateR, #8]	// Load PC position Y
	BL 	DrawImage 		// DrawImage(X, Y, player image address)

	.unreq stateR

	POP 	{r4, pc} 		// End function

//////////////////////////////////////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////////////////////////////////////

.section .data

.align 4
FrameBufferInit:

	.int 	22 * 4			    // Buffer size in bytes
	.int	0			    // Indicates a request to GPU
	.int	0x00048003		    // Set Physical Display width and height
	.int	8			    // Size of buffer
	.int	8			    // Length of value
	.int	1024			    // Horizontal resolution
	.int	768			    // Vertical resolution

	.int	0x00048004		    // Set Virtual Display width and height
	.int	8			    // Size of buffer
	.int	8			    // Length of value
	.int 	1024			    // Same as physical display width and height
	.int 	768

	.int	0x00048005		    // Set bits per pixel
	.int 	4			    // Size of value buffer
	.int	4			    // Length of value
	.int	16			    // Bits per pixel value

	.int	0x00040001		    // Allocate framebuffer
	.int	8			    // Size of value buffer
	.int	8			    // Length of value
FrameBuffer:
	.int	0			    // Value will be set to framebuffer pointer
	.int	0			    // Value will be set to framebuffer size

	.int	0			    // End tag

.align 4
.globl FrameBufferPointer

FrameBufferPointer:
	.int	0


font:	.incbin	"font.bin"
