//////////////////////////////////////////////////////
// CPSC 359
// Assignment 3
// Students:
// * Marc-Andre Fichtel, 30014709
// * Cardin Chen, 10161477
//
// Program does the following:
// * Take input from a SNES controller
// * Display the input Button
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
	MOV     sp, #0x8000 	                	       //Initialize sp
	BL	    EnableJTAG 	                	           // Enable JTAG
	BL	    InitUART 				                   // Set up UART

    MOV     r0, #9                          	// Initialize GPIO9 to output
    BL      Init_GPIO
    MOV     r0, #10                         	// Initialize GPIO10 to input
    BL      Init_GPIO
    MOV     r0, #11                         	// Initialize GPIO11 to output
    BL      Init_GPIO

    LDR     r0, =author                    		// Print author names
    MOV     r1, #59 				            // Buffer: Author String is this many Bits long
    BL     WriteStringUART 				        // Print authors' names

User_Prompt:
	LDR     r0, =Press_Button
    MOV     r1, #26                         	// Length of press Button message
    BL      WriteStringUART

Read_Buttons:
	BL      Read_SNES                        	// Read input from SNES controller
    CMP     r0,r10    	                      	// Check if previous AND current state are the same
    Beq     Same_State                       	// Branch to Same_State
    MOV     r10,r0           	               	// Previous state <= Current state
  	MOV     r6, r0                          	// r6 <= Current state
	MOV     r4, #0                          	// Initalize counter
	MOV     r5, #1                          	// r5 = 000000001
 	MVN     r5, r5                          	// r5 = 1111111110

Check_Pressed_Buttons:
	BIC     r7, r6, r5                      	// BIC r6 with r5
  	CMP     r7, #0                          	// Check if a Button is pressed
	Beq     Button_Pressed                   	// If some Button is pressed Branch to Button_Pressed
	LSR	r6, #1                          	// Check if next Bit is pressed
	ADD	r4, #1 					// Counter++
	CMP	r4, #11                         	// If (counter <= 11)
	Ble	Check_Pressed_Buttons 			// Loop
	B       Read_Buttons 				// Else go to Read_Buttons

Button_Pressed:
	MOV	r0, r4					// Move counter into r0
	BL	Print_Button 				// Print Button with index r0
	B       User_Prompt 	                	// Prompt user for next input

Same_State:
    ADD     r3, #30                         	// Wait 30 micro seconds
    BL      Wait                            	// wait(30)
    B       Read_Buttons                     	// Branch to Read_Buttons

haltLoop$:
	B	haltLoop$ 				// Exit program


//////////////////////////////////////////////////////
// Initialize GPIO
// Inputs:
// r0: GPIO pin numBer
// r1: Function code
//////////////////////////////////////////////////////

Init_GPIO:
    PUSH    {lr} 					// Start function
    CMP     r0, #9
    Beq     Set_Latch                        	// Jump to init latch (GPIO9)
    CMP     r0, #10
    Beq     Set_Data                         	// Jump to init data (GPI10)
    CMP     r0, #11
    Beq     Set_Clock                        	// Jump to init clock (GPI11)

Set_Latch:
    LDR     r0, =0x3F200000                 	// Address of GPFSEL0
    LDR     r1, [r0]
    MOV     r2, #7                          	// Bit clears (0111)
    LSL     r2, #27
    BIC     r1, r2                          	// Clearing pin 9 Bits
    MOV     r3 , #1
    LSL     r3, #27
    ORR     r1, r3
    STR     r1, [r0]                         	// Storing value Back to GPFSEL0
    B       exit

Set_Data:
    LDR     r0, =0x3F200004                  	// Address of GPFSEL1
    LDR     r1, [r0]
    MOV     r2, #7
    BIC     r1, r2                           	// Bit clears (0111)
    MOV     r3 , #0
    ORR     r1, r3
    STR     r1, [r0]                         	// Stores value Back to GPFSEL1
    B       exit

Set_Clock:
    LDR     r0, =0x3F200004                  	// Address for GPFSEL1
    LDR     r1, [r0]
    MOV     r2, #7
    LSL     r2, #3                           	// Shift left By 3
    BIC     r1, r2                           	// Bit clears (0111)
    MOV     r3 , #1
    LSL     r3, #3
    ORR     r1, r3
    STR     r1, [r0]                         	// Stores value Back to GPFSEL1
    B       exit

exit:
    POP     {lr} 					// End function
	MOV	pc, lr

//////////////////////////////////////////////////////
// Print a Button message
// Inputs:
// r0: Message to Be printed
//////////////////////////////////////////////////////

Print_Button:
    PUSH	{r6, lr} 				// Start function
    LDR     r6, =Button_Message 			// Get button string array base address
    MOV     r1, #32                          	// Each button message has length 32 bits
    MUL     r0, r1                           	// Multiply index by the String index
    CMP     r0, #96                          	// start button = 128
    Beq     Start_Button_Pressed 			// If start button was pressed, branch here
    ADD     r6, r0 					// Else get button message offset
    MOV     r0, r6
    MOV     r1, #32
    Bl      WriteStringUART 			// Print the correct button string
    POP     {r6, lr} 				// End function
    MOV     pc, lr

Start_Button_Pressed:
    LDR	r0, =Done_Program
    MOV	r1, #24
    Bl      WriteStringUART
    B 	Loop_Program
Loop_Program:
	B 	Loop_Program

//////////////////////////////////////////////////////
// Read Data (GPIO10)
// Returns:
// 0: Button was pressed or
// 1: Button was not pressed
//////////////////////////////////////////////////////

Read_Data:
	PUSH 	{lr} 					// Start function
	MOV 	r0, #10			         	// 10 for GPIO10
	LDR 	r1, =0x3F200000			 	// Base GPIO Address
	LDR 	r2, [r1, #52]			 	// GPLEV0
	MOV 	r3, #1
	LSL 	r3, r0
	AND 	r2, r3
	TEQ 	r2, #0
	MOVeq   r0, #0				 	// Return 0 if button was pressed
	MOVne   r0, #1				 	// Return 1 if button was not pressed
	POP 	{lr} 					// End function
	MOV     pc, lr

//////////////////////////////////////////////////////
// Read SNES controller
// Returns:
// 0: State of all Buttons (the sampling)
//////////////////////////////////////////////////////

Read_SNES:
	PUSH 	{r4,r5,r6,lr} 				// Start function
    MOV     r6, #0                           	// Buttons initially empty
	MOV 	r0, #1				 	// write_clock(1)
	Bl      Write_Clock
	MOV 	r0, #1	 			 	// write_latch(1)
	Bl	    Write_Latch
    MOV     r3, #12                          	// wait(12)
	Bl 	   Wait                             	// Wait 12ms
	MOV 	r0, #0				 	// write_latch(0)
	Bl	    Write_Latch
	MOV 	r5, #0 			         	// counter = 0

Pulse_Loop:

        ADD     r3, #6                          	// wait(6)
        Bl      Wait                            	// Wait 6ms
        MOV     r0, #0			        	// write_clock(0)
        Bl      Write_Clock
        MOV     r3, #6                          	// wait(6)
        Bl      Wait                            	// Wait 6ms
        Bl      Read_Data
        CMP     r0, #1					// Check if Bit is 1 or 0
        Bne     Check_Next_Bit 				// If 0, check next bit
        MOV     r2, #1                          	// Else r2 = 1
        LSL     r2, r5	                        	// LSL to the correct index
        ORR     r6, r2                          	// Add 1 (button is not pressed)

Check_Next_Bit:
	MOV     r0 ,#1			        	// write_clock(1)
	Bl      Write_Clock
	ADD     r5, #1 					// counter++
	CMP     r5, #16 				// if counter < 16
	Blt     Pulse_Loop                       	// loop, else...

Return_Bits:
        MOV     r0, r6					// Return button sampling
        POP     {r4, r5,r6, lr} 			// End function
        MOV 	pc, lr

//////////////////////////////////////////////////////
// Wait
// Inputs:
// r3: Time to wait (in microseconds)
//////////////////////////////////////////////////////

Wait:
        PUSH    {lr} 					// Start function
        LDR     r0, =0x3F003004                 	// Addess of clock
        LDR     r1, [r0]
        ADD     r1, r3

Wait_Loop:
        LDR     r2, [r0]
        CMP     r1, r2					// Stop when clock = r1
        Bhi     Wait_Loop
        POP     {lr} 					// End function
        MOV     pc, lr

//////////////////////////////////////////////////////
// Write to Clock (GPIO11)
// Inputs:
// r0: Bit to write
//////////////////////////////////////////////////////

Write_Clock:
        PUSH    {lr} 					// Start function
        MOV     r1,r0
        MOV     r0, #11
        LDR     r2, =0x3F200000                 	// Address to Base GPIO register
        MOV     r3, #1
        LSL     r3, r0
        TEQ     r1, #0
        STReq   r3, [r2, #40]                   	// If 0 then clear GPCLR0
        STRne   r3, [r2, #28]                   	// If 1 then set GPSET0
        POP     {lr} 					// End function
        MOV	    pc, lr

//////////////////////////////////////////////////////
// Write to Latch (GPIO9)
// Inputs:
// r0: Bit to write
//////////////////////////////////////////////////////

Write_Latch:
        PUSH    {lr} 					// Start function
        MOV     r1,r0
        MOV     r0, #9
        LDR     r2, =0x3F200000                 	// Address to Base GPIO register
        MOV     r3, #1
        LSL     r3, r0
        TEQ     r1, #0
        STReq   r3, [r2, #40]                   	// If 0 then clear GPCLR0
        STRne   r3, [r2, #28]                   	// If 1 then set GPSET0
        POP     {lr}
        MOV	    pc, lr 					// End function

//////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////

.section .data

author:
        .ascii  "\r\nCreated By: Created By Cardin Chen and Marc-Andre Fichtel"         // Size: 59

Press_Button:
        .ascii  "\r\nPlease press a Button..."                  // Size: 26

Button_Message:
        .ascii "\n\rYou Pressed B                 ", "\n\rYou Pressed Y                 ", "\n\rYou Pressed SELECT            ", "\n\rYou Pressed START             ", "\n\rYou Pressed UP                ", "\n\rYou Pressed DOWN              ", "\n\rYou Pressed LEFT              ", "\n\rYou Pressed RIGHT             ", "\n\rYou Pressed A                 ", "\n\rYou Pressed X                 ", "\n\rYou Pressed LEFT BUMPER       ", "\n\rYou Pressed RIGHT BUMPER      "                           // Size: 32


Done_Program:
	.ascii	"\r\nTerminating program..."					// Size: 24
