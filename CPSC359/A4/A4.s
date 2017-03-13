//////////////////////////////////////////////////////
// CPSC 359
// Assignment 4
// Students:
// * Marc-Andre Fichtel, 30014709
// * Cardin Chen, 10161477
//
// Program is the classic Super Mario Bros. game
//
//////////////////////////////////////////////////////

.section    .init
.global     _start

_start:
    B       main

.section .text

//////////////////////////////////////////////////////
// Main
//////////////////////////////////////////////////////

main:
	MOV     sp, #0x8000 	                	          // Initialize sp
	BL	    EnableJTAG 	                	            // Enable JTAG
  
  MOV     r0, #9                          	        // Initialize GPIO9 to output
  BL      Init_GPIO
  MOV     r0, #10                         	        // Initialize GPIO10 to input
  BL      Init_GPIO
  MOV     r0, #11                                 	// Initialize GPIO11 to output
  BL      Init_GPIO
  
haltLoop$:
	B	haltLoop$ 				                              // Exit program
  
//////////////////////////////////////////////////////
// Initialize GPIO
// Inputs:
// r0: GPIO pin number
// r1: Function code
//////////////////////////////////////////////////////

Init_GPIO:
  PUSH    {lr} 					                            // Start function
  CMP     r0, #9
  Beq     Set_Latch                        	        // Jump to init latch (GPIO9)
  CMP     r0, #10
  Beq     Set_Data                         	        // Jump to init data (GPI10)
  CMP     r0, #11
  Beq     Set_Clock                        	        // Jump to init clock (GPI11)

Set_Latch:
  LDR     r0, =0x3F200000                 	        // Address of GPFSEL0
  LDR     r1, [r0]
  MOV     r2, #7                          	        // Bit clears (0111)
  LSL     r2, #27
  BIC     r1, r2                          	        // Clearing pin 9 Bits
  MOV     r3 , #1
  LSL     r3, #27
  ORR     r1, r3
  STR     r1, [r0]                         	        // Storing value Back to GPFSEL0
  B       exit

Set_Data:
  LDR     r0, =0x3F200004                  	        // Address of GPFSEL1
  LDR     r1, [r0]
  MOV     r2, #7
  BIC     r1, r2                           	        // Bit clears (0111)
  MOV     r3 , #0
  ORR     r1, r3
  STR     r1, [r0]                         	        // Stores value Back to GPFSEL1
  B       exit

Set_Clock:
  LDR     r0, =0x3F200004                  	        // Address for GPFSEL1
  LDR     r1, [r0]
  MOV     r2, #7
  LSL     r2, #3                           	        // Shift left By 3
  BIC     r1, r2                           	        // Bit clears (0111)
  MOV     r3 , #1
  LSL     r3, #3
  ORR     r1, r3
  STR     r1, [r0]                         	        // Stores value Back to GPFSEL1
  B       exit

exit:
  POP     {lr} 					// End function
  MOV	pc, lr
  
//////////////////////////////////////////////////////
// Read Data (GPIO10)
// Returns:
// r0: 0 if Button was pressed or 1 if Button was not pressed
//////////////////////////////////////////////////////

Read_Data:
  PUSH 	{lr} 					                              // Start function
	MOV 	r0, #10			         	                      // 10 for GPIO10
	LDR 	r1, =0x3F200000			 	                      // Base GPIO Address
	LDR 	r2, [r1, #52]			 	                        // GPLEV0
	MOV 	r3, #1
	LSL 	r3, r0
	AND 	r2, r3
	TEQ 	r2, #0
	MOVeq   r0, #0				 	                          // Return 0 if button was pressed
	MOVne   r0, #1				 	                          // Return 1 if button was not pressed
	POP 	{lr} 					                              // End function
	MOV     pc, lr
  
//////////////////////////////////////////////////////
// Wait
// Inputs:
// r3: Time to wait (in microseconds)
//////////////////////////////////////////////////////

Wait:
  PUSH    {lr} 					                            // Start function
  LDR     r0, =0x3F003004                 	        // Addess of clock
  LDR     r1, [r0]
  ADD     r1, r3

Wait_Loop:
  LDR     r2, [r0]
  CMP     r1, r2					                          // Stop when clock = r1
  Bhi     Wait_Loop
  POP     {lr} 					                            // End function
  MOV     pc, lr

//////////////////////////////////////////////////////
// Write to Clock (GPIO11)
// Inputs:
// r0: Bit to write
//////////////////////////////////////////////////////

Write_Clock:
  PUSH    {lr} 					                            // Start function
  MOV     r1,r0
  MOV     r0, #11
  LDR     r2, =0x3F200000                 	        // Address to Base GPIO register
  MOV     r3, #1
  LSL     r3, r0
  TEQ     r1, #0
  STReq   r3, [r2, #40]                   	        // If 0 then clear GPCLR0
  STRne   r3, [r2, #28]                   	        // If 1 then set GPSET0
  POP     {lr} 					                            // End function
  MOV	    pc, lr

//////////////////////////////////////////////////////
// Write to Latch (GPIO9)
// Inputs:
// r0: Bit to write
//////////////////////////////////////////////////////

Write_Latch:
  PUSH    {lr} 					                            // Start function
  MOV     r1,r0
  MOV     r0, #9
  LDR     r2, =0x3F200000                 	        // Address to Base GPIO register
  MOV     r3, #1
  LSL     r3, r0
  TEQ     r1, #0
  STReq   r3, [r2, #40]                   	        // If 0 then clear GPCLR0
  STRne   r3, [r2, #28]                   	        // If 1 then set GPSET0
  POP     {lr}
  MOV	    pc, lr 					                          // End function
  
//////////////////////////////////////////////////////
// Initialize display
//////////////////////////////////////////////////////

Init_Display:
  PUSH    {lr} 					                            // Start function
  
  // Set resolution
  
  // Set bit depth
  
  // Get frame buffer pointer:
  // ~ Create data structure with init information
  MOV r0, =frameBufferInfo    // Get frameBufferInfo address
  LSL r0, #4                  // msg[4..31] = frameBufferInfo
  ORR r0, #8                  // msg[0..3] = 8
  MOV r1, #1                  // r1 = 1
  LSL r1, #30                 // r1[30] = 1
  ORR r0, r1                  // msg[30] = 1 (forces GPU to not cache FBI)
  
  // ~ Wait until mailbox can accept messages
  // ~ Write address of init struct to mailbox frame buffer channel
  // ~ Wait for response from mailbox
  // ~ Wait for frame buffer pointer in init struct to be set
  
  POP     {lr} 					                            // End function

//////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////

.section .data
.align 4

frameBufferInfo:
  .int 22 * 4         // Buffer size in bytes
  .int 0              // Indicates a request to the GPU
  
  .int 0x00048003     // (Tag ID) Set physical display width and height
  .int 8              // Buffer size
  .int 8              // length of value
  .int 1024           // Resolution horiz
  .int 768            // Resolution vert
  
  .int 0x00048004     // (Tag ID) Set virtual display width and height
  .int 8              // Buffer size
  .int 8              // length of value
  .int 1024           // Resolution horiz
  .int 768            // Resolution vert
  
  .int 0x00048005     // (Tag ID) Set bits per pixel
  .int 4              // Buffer value size
  .int 4              // length of value
  .int 16             // bpp (bits per pixel) value
  
  .int 0x00040001     // (Tag ID) Allocate frame buffer
  .int 8              // Buffer value size
  .int 8              // length of value
  
frameBuffer:
  .int 0              // Value will be set to frame buffer pointer
  .int 0              // Value will be set to frame buffer size
  .int 0              // (End Tag) Indicates end of buffer
  
Mailbox0: 
  .int 0x3f00B880     // Address of mailbox 0
