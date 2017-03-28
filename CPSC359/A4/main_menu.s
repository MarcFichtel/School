.section .text

////////////////////////////////////////////////////////////////////////////////////
// Draw the main menu
////////////////////////////////////////////////////////////////////////////////////

.globl DrawMenu 			// Make function global
DrawMenu: 
	PUSH 	{lr}			// Start function 

	BL 	DrawBG 			// Draw Background
	
	MOV 	r0, #400
	MOV 	r1, #500
	MOV 	r2, =startString
	BL 	DrawString 		// Draw Start String

	MOV 	r0, #400
	MOV 	r1, #550
	MOV 	r2, =exitString
	BL 	DrawString 		// Draw Exit String

	MOV 	r0, #100
	MOV 	r1, #700
	MOV 	r2, =authorsString
	BL 	DrawString 		// Draw Exit String

	LDR 	r0, =mainMenuSelect	// Load menu selection address
	MOV 	r1, #1 			// r1 = 1
	STR 	r1, [r0] 		// Clear selection
	BL 	DrawSelector 		// Display selector

	POP 	{pc} 			// End function

////////////////////////////////////////////////////////////////////////////////////
// Draw the main menu selector
////////////////////////////////////////////////////////////////////////////////////

.globl DrawSelector 			// Make function global
DrawSelector:
	PUSH 	{r4-r5, lr} 		// Start function

	LDR	r5, =mainMenuSelect	// Load selection address
	LDR	r4, [r5]		// Load selection value
	CMP	r4, #0			// If Exit was selected
	Beq	exit 			// Move selector to exit
	
	// Draw new selector
	LDR	r0, =140		
	LDR	r1, =250		
	LDR	r2, =0xF000		
	LDR	r3, =130		
	BL	DrawSelector		// DrawSelector(X, Y, Color, Text width)
	
	// Erase previous selector
	LDR	r0, =140		
	LDR	r1, =300		
	LDR	r2, =0x0000		
	LDR	r3, =120		
	BL	DrawSelector		
	
	MOV	r0, #0			// Clear selection
	STR	r0, [r5]		// Store selection
	B	done			// Finish function
exit:
	// Draw new selector	
	LDR	r0, =140		
	LDR	r1, =300		
	LDR	r2, =0xF000		
	LDR	r3, =120		
	BL	DrawSelector		// DrawSelector(X, Y, Color, Text width)
	
	// Erase previous selector
	LDR	r0, =140		
	LDR	r1, =250		
	LDR	r2, =0x0000		
	LDR	r3, =130		
	BL	DrawSelector		
	
	MOV	r0, #1 			// Track new selection
	STR	r0, [r5]		// Store the selection
done:	
	POP 	{r4-r5, pc} 		// End function

////////////////////////////////////////////////////////////////////////////////////
// Data Section
////////////////////////////////////////////////////////////////////////////////////

.section .data
.align 4

.globl startString
startString: 	.asciz 	"START Game"

.globl exitString
exitString: 	.asciz 	"EXIT Game"

.globl authorsString
authorsString: 	.asciz 	"By Olga Bogdanova, Cardin Chen, & Marc-Andre Fichtel"

.globl mainMenuSelect
mainMenuSelect: .int 	0
