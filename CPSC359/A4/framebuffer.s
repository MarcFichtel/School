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

	ADD	r4, r0, r1, LSL #10 	// r4 = (y * 1024) + x = x + (y << 10)
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
// Draw the BG Image
/////////////////////////////////////////////////////////////////////

.globl DrawBGImg 			// Make function global
DrawBGImg:
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

bgimg:
	MOV 	r0, pointX
	MOV 	r1, pointY
	LDRH 	r2, [img], #2
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size, loop, else...
	Blt 	bgimg

	SUB 	pointX, #1024 		// X -= 40
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border, loop, else...
	Blt 	bgimg

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq img
	.unreq borderX
	.unreq borderY

	POP 	{r4-r8, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw the BG
// Inputs:
// ~ r0: StartX
// ~ r1: StartY
// ~ r2: EndX
// ~ r3: EndY
//
// TODO find a blue color code for the sky BG
//
/////////////////////////////////////////////////////////////////////

.globl DrawBG 				// Make function global
DrawBG:
	PUSH 	{r4-r9, lr} 		// Start function

	// Name a few registers
	startX 	.req r4
	startY 	.req r5
	color 	.req r6
	endX 	.req r7
	endY 	.req r8
	width 	.req r9

	// Setup
	MOV 	startX, r0 		
	MOV 	startY, r1 		
	MOV 	color, #0x0000FF	 // TODO insert blue color code here
	MOV 	endX, r2 		
	MOV 	endY, r3 
	SUB 	width, endX, startX 	// Width = EndX - StartX		

bg:
	MOV 	r0, startX
	MOV 	r1, startY
	MOV 	r2, color
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	startX, #1 		// X++
	CMP 	startX, endX 		// If startX < endX, loop, else...
	Blt 	bg

	SUB 	startX, width 		// X -= width
	ADD 	startY, #1 		// Y++
	CMP 	startY, endY 		// If startY < endY, loop, else done
	Blt 	bg

	// Unname a few registers
	.unreq startX
	.unreq startY
	.unreq color
	.unreq endX
	.unreq endY
	.unreq width

	POP 	{r4-r9, pc} 		// End function


//////////////////////////////////////////////////////////////////////
//
// Draw the first scene of first map
//
// Takes object positions from game state
//
/////////////////////////////////////////////////////////////////////

.globl DrawMap1_1 				// Make function global
DrawMap1_1:
	PUSH 	{r4-r6, lr} 		// Start function

	LDR 	r4, =map11blocks 	// Load address of map 1-1 blocks

// Draw bricks
	// Draw Brick 1
	LDR 	r0, [r4] 			// Load brick 1 X
	LDR 	r1, [r4, #4] 		// Load brick 1 Y
	LDR 	r2, =brick 			// Load brick image address
	BL 		DrawImage 			// Draw brick 1

	// Draw Brick 2
	LDR 	r0, [r4, #8] 		// Load brick 2 X
	LDR 	r1, [r4, #12] 		// Load brick 2 Y
	LDR 	r2, =brick 			// Load brick image address
	BL 		DrawImage 			// Draw brick 2

	// Draw Brick 3
	LDR 	r0, [r4, #16] 		// Load brick 3 X
	LDR 	r1, [r4, #20] 		// Load brick 3 Y
	LDR 	r2, =brick 			// Load brick image address
	BL 		DrawImage 			// Draw brick 3

	// Draw Brick 4
	LDR 	r0, [r4, #24] 		// Load brick 4 X
	LDR 	r1, [r4, #28] 		// Load brick 4 Y
	LDR 	r2, =brick 			// Load brick image address
	BL 		DrawImage 			// Draw brick 4

// Draw coin blocks
	// Draw coin block 1
	LDR 	r0, [r4, #32] 		// Load coin block 1 X
	LDR 	r1, [r4, #36] 		// Load coin block 1 Y
	LDR 	r2, =coinblock 		// Load brick image address
	BL 		DrawImage 			// Draw coin block 1

	// Draw coin block 2
	LDR 	r0, [r4, #40] 		// Load coin block 2 X
	LDR 	r1, [r4, #44] 		// Load coin block 2 Y
	LDR 	r2, =coinblock 		// Load brick image address
	BL 		DrawImage 			// Draw coin block 2

	// Draw coin block 3
	LDR 	r0, [r4, #48] 		// Load coin block 3 X
	LDR 	r1, [r4, #52] 		// Load coin block 3 Y
	LDR 	r2, =coinblock 		// Load brick image address
	BL 		DrawImage 			// Draw coin block 3

	// Draw floor
	LDR 	r5, [r4, #56] 		// Load floor X (0)
	LDR 	r6, [r4, #60]		// Load floor Y (640)
drawFloor:
	MOV 	r0, r5 		
	MOV 	r1, r6 		
	LDR 	r2, =floor 			// Load floor image address
	BL 		DrawImage 			// Draw floor

	ADD 	r5, #64 			// X++
	CMP 	r5, #1024 			// If X < 1024
	Blt 	drawFloor 			// Loop

	MOV 	r5, #0 				// Reset X
	ADD 	r6, #64 			// Y++
	CMP 	r6, #768 			// If Y < 768
	Blt 	drawFloor 			// Loop

	// Draw Goomba 1
	LDR 	r0, [r4, #68] 		// Load goomba 1 X
	LDR 	r1, [r4, #72] 		// Load goomba 1 Y
	LDR 	r2, =goomba1 		// Load goomba image address
	BL 		DrawImage 			// Draw goomba 1

	POP 	{r4-r6, pc} 		// End function


//////////////////////////////////////////////////////////////////////
//
// Setup Scene
//
/////////////////////////////////////////////////////////////////////

.globl SetupScene 		// Make function global
SetupScene:
	PUSH 	{lr} 		// Start function

	MOV 	r0, #0
	MOV 	r1, #0
	MOV 	r2, #1024
	MOV 	r3, #768
	BL 	DrawBG 		// Draw in-game BG (TODO find blue color code for this function)
	BL 	DrawMap1_1 	// Draw in-game objects
	BL 	DrawPC 		// Draw player

	POP 	{pc} 		// End function

//////////////////////////////////////////////////////////////////////
//
// Draw start menu
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Image Address
//
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

//////////////////////////////////////////////////////////////////////
// Draw pause menu
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Image Address
/////////////////////////////////////////////////////////////////////

.globl DrawPauseMenu		  	// Make function global
DrawPauseMenu:
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

imgLoopStart1:
	MOV 	r0, pointX
	MOV 	r1, pointY
	LDRH 	r2, [img], #2
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size
	Blt 	imgLoopStart1 		// Loop

	SUB 	pointX, #320 		// X -= 320
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border
	Blt 	imgLoopStart1 		// Loop

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

	BL 	DrawBGImg 			// Draw Background

	MOV 	r0, #352
	MOV 	r1, #500
	LDR 	r2, =menuPointerSTART
	BL 	DrawStartMenu 		// Draw Start menu options

	MOV 	r0, #20
	MOV 	r1, #700
	LDR 	r2, =authorsString
	BL 	DrawString

	POP 	{pc} 			// End function

////////////////////////////////////////////////////////////////////////////////////
//
// Draw game scene
//
// This function redraws the background after the player or an enemy move
//
////////////////////////////////////////////////////////////////////////////////////

.globl DrawScene
DrawScene:
	PUSH 	{r4-r10, lr}		// Start function
	
	BL 	DrawPC 			// Draw the player character
	BL 	DrawEnemy
	// Draw Score, Coins, Lives text
	MOV 	r0, #10
	MOV 	r1, #10
	LDR 	r2, =scoreText
	BL DrawString
	MOV 	r0, #10
	MOV 	r1, #50
	LDR 	r2, =coinsText
	BL DrawString	
	MOV 	r0, #10
	MOV 	r1, #90
	LDR 	r2, =livesText
	BL DrawString	

	// Draw actual score, coins, lives behind text
	LDR 	r3, =state
	LDR 	r0, [r3, #56]
	LDR 	r1, =scoreActual
	BL 	IntegerToString 	// IntToString (score, score_string)
	MOV 	r0, #200
	MOV 	r1, #10
	LDR 	r2, =scoreActual
	BL DrawString	

	LDR 	r3, =state
	LDR 	r0, [r3, #60]
	LDR 	r1, =coinsActual
	BL 	IntegerToString 	// IntToString (coins, coins_string)
	MOV 	r0, #200
	MOV 	r1, #50
	LDR 	r2, =coinsActual
	BL DrawString	

	LDR 	r3, =state
	LDR 	r0, [r3, #64]
	LDR 	r1, =livesActual
	BL 	IntegerToString 	// IntToString (lives, lives_string)
	MOV 	r0, #200
	MOV 	r1, #90
	LDR 	r2, =livesActual
	BL DrawString	

	// Name a few registers
	pX 	.req r4
	mSpeed	.req r5
	mJump 	.req r6
	mFall 	.req r7
	mLeft 	.req r8
	mRight 	.req r9
	pY 	.req r10

	LDR 	r0, =state
	LDR 	pX, [r0, #4] 		// Load player position X
	LDR 	pY, [r0, #8] 		// Load player position Y
	LDR 	mSpeed, [r0, #12] 	// Load player move speed
	LDR 	mJump, [r0, #20] 	// Load player jumping flag
	LDR 	mFall, [r0, #24] 	// Load player falling flag
	LDR 	mLeft, [r0, #32] 	// Load player moving left flag
	LDR 	mRight, [r0, #36] 	// Load player moving right flag

	ADD 	r1, mJump, mFall
	ADD 	r1, mLeft
	ADD 	r1, mRight
	CMP 	r1, #0
	Beq 	doneDrawScene 		// If all flags are false, finish function

// Redraw space player moved away from
	// Moving Right
	CMP 	mRight, #1 		// If moving right
	SUBeq 	r0, pX, mSpeed	 	// DrawBG_StartX = PlayerX - MoveSpeed
	MOVeq 	r1, pY 			// DrawBG_StartY = PlayerY
	MOVeq 	r2, pX 			// DrawBG_EndX = PlayerX
	ADDeq 	r3, pY, #64 		// DrawBG_EndY = PlayerY + 64
	BLeq 	DrawBG 			// Draw the background	
	
	// Moving Left
	CMP 	mLeft, #1 		// If moving left
	ADDeq 	r0, pX, #64	 	// DrawBG_StartX = PlayerX + 64
	MOVeq 	r1, pY 			// DrawBG_StartY = PlayerY
	ADDeq 	r2, r0, mSpeed 		// DrawBG_EndX = StartX + MoveSpeed
	ADDeq 	r3, pY, #64 		// DrawBG_EndY = PlayerY + 64
	BLeq 	DrawBG 			// Draw the background	

	// Rename & reload r5
	.unreq 	mSpeed 			// Unname r5
	jSpeed 	.req r5 		// Rename r5
	LDR 	r0, =state 		// Load game state address
	LDR 	jSpeed, [r0, #16] 	// Load jump speed

	// Moving Up
	CMP 	mJump, #1 		// If moving up
	MOVeq 	r0, pX	 		// DrawBG_StartX = PlayerX
	ADDeq 	r1, pY, #64 		// DrawBG_StartY = PlayerY + 64
	ADDeq 	r2, pX, #64 		// DrawBG_EndX = PlayerX + 64
	ADDeq 	r3, r1, jSpeed 		// DrawBG_EndY = StartY + JumpSpeed
	BLeq 	DrawBG 			// Draw the background

	// Moving Down
	CMP 	mFall, #1 		// If moving up
	MOVeq 	r0, pX	 		// DrawBG_StartX = PlayerX
	SUBeq 	r1, pY, jSpeed, LSL #1 	// DrawBG_StartY = PlayerY - JumpSpeed
	ADDeq 	r2, pX, #64 		// DrawBG_EndX = PlayerX + 64
	MOVeq 	r3, pY	 		// DrawBG_EndY = PlayerY
	BLeq 	DrawBG 			// Draw the background

	LDR 	r0, =state
	LDR 	r3, [r0, #12] 	// Load mSpeed

	// Moving Up & Right
	ADD 	r0, mJump, mRight	// Sum jump and right flags
	CMP 	r0, #2 			// If jump and right flag are set 
	SUBeq 	r0, pX, r3 		// DrawBG_StartX = PlayerX - move speed
	ADDeq 	r1, pY, #64 		// DrawBG_StartY = PlayerY + 64	
	MOVeq 	r2, pX 			// DrawBG_EndX = PlayerX
	ADDeq 	r3, r1, jSpeed 		// DrawBG_EndY = StartY + jump speed
	BLeq 	DrawBG 			// Draw the background	

	// Moving Up & Left
	ADD 	r0, mJump, mLeft	// Sum jump and left flags
	CMP 	r0, #2 			// If jump and right flag are set 
	ADDeq 	r0, pX, #64 		// DrawBG_StartX = PlayerX + 64
	ADDeq 	r1, pY, #64 		// DrawBG_StartY = PlayerY + 64	
	ADDeq 	r2, r0, r3		// DrawBG_EndX = StartX + move speed
	ADDeq 	r3, r1, jSpeed 		// DrawBG_EndY = StartY + jump speed
	BLeq 	DrawBG 			// Draw the background	

	// Moving Down & Right
	ADD 	r0, mFall, mRight	// Sum fall and right flags
	CMP 	r0, #2 			// If jump and right flag are set 
	SUBeq 	r0, pX, r3 		// DrawBG_StartX = PlayerX - move speed
	SUBeq 	r1, pY, jSpeed 		// DrawBG_StartY = PlayerY - jump speed
	MOVeq 	r2, pX			// DrawBG_EndX = PlayerX
	MOVeq 	r3, pY 			// DrawBG_EndY = PlayerY
	BLeq 	DrawBG 			// Draw the background	

	// Moving Down & Left
	ADD 	r0, mFall, mLeft	// Sum fall and left flags
	CMP 	r0, #2 			// If jump and right flag are set 
	ADDeq 	r0, pX, #64 		// DrawBG_StartX = PlayerX + 64
	SUBeq 	r1, pY, jSpeed 		// DrawBG_StartY = PlayerY - jump speed
	ADDeq 	r2, r0, r3		// DrawBG_EndX = StartX + move speed
	MOVeq 	r3, pY 			// DrawBG_EndY = PlayerY
	BLeq 	DrawBG 			// Draw the background	

doneDrawScene:
	.unreq 	pX
	.unreq 	pY
	.unreq 	jSpeed
	.unreq 	mFall
	.unreq 	mJump
	.unreq 	mRight
	.unreq 	mLeft

	POP 	{r4-r10, pc} 		// End function

////////////////////////////////////////////////////////////////////////////////////
//
// Draw Enemy
//
////////////////////////////////////////////////////////////////////////////////////

.globl DrawEnemy		// Make function global
DrawEnemy:
	PUSH 	{lr}			// Start function
	LDR 	r3, =map11blocks
	LDR 	r0, [r0, #68] 			// goomba x 
	LDR 	r1, [r0, #72] 			// goomba y 
	MOV 	r0, 
	MOV 	r1, #500

	LDR 	r2, =pausePointerRESTART
	BL 	DrawPauseMenu

	POP 	{pc} 			// End function

////////////////////////////////////////////////////////////////////////////////////
//
// Draw player character
//
// Draw the correct player sprite to the current player position in game state 
// depending on movement flags
//
////////////////////////////////////////////////////////////////////////////////////

.globl DrawPC
DrawPC:
	PUSH 	{r3-r8, lr}		// Start function

	// Name a few registers
	stateR 	.req r3
	jumpF	.req r4
	fallF	.req r5
	mLeft	.req r6
	mRight	.req r7
	wAnim 	.req r8

	LDR 	stateR, =state 		// Load state address
	LDR 	jumpF, [stateR, #20]	// Load jump flag
	LDR 	fallF, [stateR, #24] 	// Load fall flag
	LDR 	mLeft, [stateR, #32] 	// Load move left flag
	LDR 	mRight, [stateR, #36] 	// Load move right flag
	LDR 	wAnim, [stateR, #76] 	// Load walk animation state

	ADD 	r2, jumpF, fallF 	// Sum jump and fall flags
	CMP 	r2, #0 			// If jumping or falling
	LDRgt 	r2, =luigijump 		// Load jump img address
	Bgt 	donePC 			// Branch to donePC

	ADD 	r2, mLeft, mRight 	// Sum up left and right move flags
	CMP 	r2, #0 			// If not moving sideways
	LDReq 	r2, =luigi1 		// Load idle image	
	Beq 	donePC 			// Branch to doneP

	CMP 	mLeft, #1 		// If moving left
	Beq 	walkLeft 		// Branch to walkLeft

walkRight:
	CMP 	wAnim, #0 		// Check walk animation state
	MOVeq	r2, #1 			
	LDReq 	stateR, =state 		// Load state address	
	STReq  	r2, [stateR, #76] 	// Toggle walk anim flag
	LDReq 	r2, =luigi1 		// Load 1st img address into r2
	Beq 	donePC 			// Finish function	

	MOVne 	r2, #0	 
	LDRne 	stateR, =state 		// Load state address	
	STRne 	r2, [stateR, #76]	// Toggle walk anim flag
	LDRne 	r2, =luigi2 		// Load 2nd image address into r2
	Bne 	donePC 			// Finish function

walkLeft:
	CMP 	wAnim, #0 		// Check walk animation state
	MOVeq	r2, #1 			
	LDReq 	stateR, =state 		// Load state address	
	STReq  	r2, [stateR, #76] 	// Toggle walk anim flag
	LDReq 	r2, =luigi1rev 		// Load 1st reversed img address into r2
	Beq 	donePC 			// Finish function

	MOVne 	r2, #0	 		
	LDRne 	stateR, =state 		// Load state address	
	STRne 	r2, [stateR, #76]	// Toggle walk anim flag
	LDRne 	r2, =luigi2rev		// Load 2nd reversed image address into r2
	Bne 	donePC 			// Finish function

donePC:
	LDR 	stateR, =state 		// Load state address
	LDR 	r1, [stateR, #8]	// Load PC position Y
	LDR 	r0, [stateR, #4] 	// Load PC position X
	BL 	DrawImage 		// DrawImage(X, Y, player image address)

	// Unname a few registers
	.unreq stateR
	.unreq jumpF
	.unreq fallF
	.unreq mLeft
	.unreq mRight

	POP 	{r3-r8, pc} 		// End function

////////////////////////////////////////////////////////////////////////////////////
// Draw pause menu
////////////////////////////////////////////////////////////////////////////////////

.globl DrawPauseMenuOptions 		// Make function global
DrawPauseMenuOptions:
	PUSH 	{lr}			// Start function

	MOV 	r0, #352
	MOV 	r1, #500

	LDR 	r2, =pausePointerRESTART
	BL 	DrawPauseMenu

	POP 	{pc} 			// End function


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

scoreText: 	.asciz "Score: "
coinsText: 	.asciz "Coins: "
livesText: 	.asciz "Lives: "
scoreActual: 	.asciz ""
coinsActual: 	.asciz ""
livesActual: 	.asciz ""
