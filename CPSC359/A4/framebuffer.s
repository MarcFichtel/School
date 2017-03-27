.section .text

//////////////////////////////////////////////////////////////////////////////////////
// Initialize the FrameBuffer using the FrameBufferInit structure
// Outputs:
// ~ r0: 0 on failure, framebuffer pointer on success
//////////////////////////////////////////////////////////////////////////////////////

.globl InitFrameBuffer 			// Make function global

InitFrameBuffer:
    	PUSH    {r4, lr} 		// Start function
	
    	LDR     r4, =FrameBufferInfo    // Load framebuffer info address
    	MOV     r0, r4                  // Store fbi addr as mail message
	ADD	r0, #0x40000000		// Set bit 30 --> tell GPU to not cache any changes
    	MOV     r1, #1                	// MB channel 1
    	BL      MBWrite                 // Write message
    	MOV     r0, #1                  // MB channel 1
    	BL      MBRead                  // Read message
    	TEQ     r0, #0
    	MOVne   r0, #0
    	POPne   {r4, pc}                // Return 0 if message from mailbox is 0
    
pointerWait$:
    	LDR     r0, [r4, #32]
    	TEQ     r0, #0
    	Beq     pointerWait$            // Loop until the pointer is set
	LDR	r1, =FrameBufferPointer
	STR	r0, [r1]		// Store the fb pointer
    	MOV     r0, r4                 	// Return fb pointer
	
    	POP     {r4, pc}                // End function

//////////////////////////////////////////////////////////////////////
// Draw Pixel
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Color
/////////////////////////////////////////////////////////////////////

.globl DrawPixel 		  		// Make function global

DrawPixel:
	PUSH	{r4, lr} 			// Start function
	
	ADD	r4, r0, r1, lsl #10 		// r4 = (y * 1024) + x = x + (y << 10)
	LSL	r4, #1 				// r4 *= 2 (for 16 bits per pixel = 2 bytes per pixel)
	LDR	r0, =FrameBufferPointer		// Get frame buffer pointer address
	LDR	r0, [r0] 			// Load frame buffer pointer into r0
	STRH	r2, [r0, r4] 			// Store color (hword) at framebuffer pointer + offset
	
	POP	{r4, pc}			// End function

//////////////////////////////////////////////////////////////////////
// Clear Screen (paints display black)
/////////////////////////////////////////////////////////////////////

.globl ClearScreen		// Make function global

ClearScreen:
	PUSH 	{r4-r6, lr} 	// Start function

	MOV 	r4, #0 		// X = 0
	MOV 	r5, #0 		// Y = 0
	MOV 	r6, #0 		// Color = Black

loop:
	MOV 	r0, r4 		// Reset X
	MOV 	r1, r5 		// Reset Y
	MOV 	r2, r6 		// Reset color
	BL 	DrawPixel 	// DrawPixel (X, Y, #white)

	ADD 	r4, #1 		// X++
	CMP 	r4, #1024 	// If X < 1024, loop, else...
	Blt 	loop

	MOV 	r4, #0 		// X = 0
	ADD 	r5, #1 		// Y++
	CMP 	r5, #768 	// If Y < 768, loop, else...
	Blt 	loop

	POP 	{r4-r6, pc} 	// End function

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
	PUSH 	{r4-r9, lr} 		// Start function

	// Name a few registers
	pointX 	.req r4
	pointY 	.req r5
	color 	.req r6
	size 	.req r7
	borderX .req r8
	borderY .req r9

	// Setup
	MOV 	pointX, r0 		// X = r0
	MOV 	pointY, r1 		// Y = r1
	MOV 	size, r2 		// Size = r2
	MOV 	color, r3 		// Color = r3
	MOV 	borderX, size
	ADD 	borderX, pointX 	// borderX += size 
	MOV 	borderY, size
	ADD 	borderY, pointY 	// borderY += size 

loop0:
	MOV 	r0, pointX 		// Reset X
	MOV 	r1, pointY 		// Reset Y
	MOV 	r2, color
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size, loop, else...
	Blt 	loop0

	SUB 	pointX, size 		// X -= 40
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border, loop, else...
	Blt 	loop0

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq color
	.unreq size
	.unreq borderX
	.unreq borderY

	POP 	{r4-r9, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw a Character (in white)
// ~ r0: X-coordinate
// ~ r1: Y-coordinate
// ~ r2: Character
/////////////////////////////////////////////////////////////////////

.globl DrawChar		  		// Make function global

DrawChar:
	PUSH	{r4-r10, lr} 		// Start function

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

	// name a few registers
	pointX	.req r5 
	pointY	.req r6
	strAddr .req r7

	// Setup
	MOV 	pointX, r0 	
	MOV 	pointY, r1
	MOV 	strAddr, r2

loop1:
	LDRB 	r2, [strAddr], #1 	// Load byte into r3
	CMP 	r2, #0 			// Compare current byte to null
	Beq 	done1 			// If equal, string is done
	BL 	DrawChar		// Else, DrawChar(X, Y, current_character)
	ADD 	pointX, #20		// Offset next character
	MOV 	r0, pointX 		// Reset X
	MOV 	r1, pointY 		// Reset Y
	B 	loop1 			// Then loop to next character
done1:
	// Unname a few registers
	.unreq 	pointX 	
	.unreq 	pointY
	.unreq 	strAddr

	POP	{r5-r7, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw an Image (each image is 40x40)
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

	// Setup
	MOV 	pointX, r0 		// X = r0
	MOV 	pointY, r1 		// Y = r1
	MOV 	img, r2 		// img = r3
	MOV 	size, #40 		// All images are 40x40
	MOV 	borderX, size
	ADD 	borderX, pointX 	// borderX = X + size
	MOV 	borderY, size
	ADD 	borderY, pointY 	// borderY = Y + size

loop2:
	MOV 	r0, pointX 		// Reset X
	MOV 	r1, pointY 		// Reset Y
	LDRH 	r2, [img], #2 		// Load next pixel's color
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size, loop, else...
	Blt 	loop2

	SUB 	pointX, size 		// X -= 40 (because all images are 40x40)
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border, loop, else...
	Blt 	loop2

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq img
	.unreq size
	.unreq borderX
	.unreq borderY

	POP 	{r4-r9, pc} 		// End function

//////////////////////////////////////////////////////////////////////
// Draw the BG (1024x768)
/////////////////////////////////////////////////////////////////////

.globl DrawBG				// Make function global

DrawBG:
	PUSH 	{r4-r8, lr} 		// Start function

	// Name a few registers
	pointX 	.req r4
	pointY 	.req r5
	img 	.req r6
	borderX .req r7
	borderY .req r8

	// Setup
	MOV 	pointX, #0 		// X = 0
	MOV 	pointY, #0 		// Y = 0
	LDR 	img, =startmenu 	// img = menu background image address
	MOV 	borderX, #1024 		// borderX = 1024
	MOV 	borderY, #768 		// borderY = 768

loop3:
	MOV 	r0, pointX 		// Reset X
	MOV 	r1, pointY 		// Reset Y
	LDRH 	r2, [img], #2 		// Load next pixel color
	BL 	DrawPixel 		// DrawPixel(X, Y, color)

	ADD 	pointX, #1 		// X++
	CMP 	pointX, borderX 	// If X < size, loop, else...
	Blt 	loop3

	SUB 	pointX, #1024 		// X -= 1024
	ADD 	pointY, #1 		// Y++
	CMP 	pointY, borderY 	// If Y < border, loop, else...
	Blt 	loop3

	// Unname a few registers
	.unreq pointX
	.unreq pointY
	.unreq img
	.unreq borderX
	.unreq borderY

	POP 	{r4-r8, pc} 		// End function

//////////////////////////////////////////////////////////////////////////////////////
// Write to Mailbox
// Inputs:
// ~ r0: Value (4 LSB should be 0)
// ~ r1: Channel to write to
//////////////////////////////////////////////////////////////////////////////////////

.globl MBWrite 				// Make function global

MBWrite:
	PUSH 	{lr} 			// Start function
	
    	TST     r0, #0b1111            	// If lower 4 bits of r0 != 0 (invalid message),...
    	MOVne   pc, lr                  // Return
    	CMP     r1, #15                 // If r1 > 15 (must be a valid channel)...
    	MOVhi   pc, lr                  // Return
    	MOV     r2, r0
    	LDR     r0, =0x2000B880 	// Else load mailbox
    
MBWriteWait:
    	LDR     r3, [r0, #0x18]    	// Load mailbox status
    	TST     r3, #0x80000000         // Test bit-32
    	Bne     MBWriteWait             // Loop while status bit 32 != 0
    	ADD     r2, r1                  // value += channel
    	STR     r2, [r0, #0x20]         // Store message to the mailbox write offset
    
    	POP 	{pc}			// End function

//////////////////////////////////////////////////////////////////////////////////////
// Read from Mailbox
// Inputs:
// ~ r0: Channel
// Outputs:
// ~ r0: Message
//////////////////////////////////////////////////////////////////////////////////////

.globl MBRead 					// Make function global

MBRead:
	PUSH 	{lr}				// Start function
    	CMP     r0, #15                         // If channel is invalid
    	MOVhi   pc, lr 				// Return
    	MOV     r1, r0
	LDR     r0, =0x2000B880 		// Else load mailbox
    
MBReadwait:
    	LDR     r2, [r0, #0x18]        		// Load mailbox status
    	TST     r2, #0x4000000              	// Test bit 30
    	Bne     MBReadwait                      // Loop while status bit 30 != 0
    	LDR     r2, [r0, #0]             	// Load message
    	AND     r3, r2, #0b1111           	// Mask out channel bits in message
    	TEQ     r3, r1
    	Bne     MBReadwait                      // If wrong channel, loop, else...
    	AND     r0, r2, #0xfffffff0           	// Mask out channel bits from message, store result in r0
    
	POP 	{pc}				// End function
	
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
.global FrameBufferPointer

FrameBufferPointer:
	.int	0
