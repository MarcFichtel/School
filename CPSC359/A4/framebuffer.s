.section .text

.global InitFrameBuffer

//////////////////////////////////////////////////////////////////////////////////////
// Initialize the FrameBuffer using the FrameBufferInit structure
// Code taken from tut08 example
// Returns:
// ~ r0: 0 on failure, framebuffer pointer on success
//////////////////////////////////////////////////////////////////////////////////////
 
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
