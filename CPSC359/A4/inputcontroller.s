.globl StartMenuControl
/*
	Tests for 
	return in r0: 
		0 = start game
		1 = quit game
*/
StartMenuControl:
	push 	{lr}

readStartInput:
	bl 		loopdelay 			// *** why do we loop delay ***
	bl 		ReadSNES 			// read user input 
	ldr 	r1, =0xFFFF 		// bitmask 
	teq 	r0, r1 				// check for button press
	beq 	readStartInput 			// if no buttons pressed re-read controller input 

	ldr 	r1, =0xFEFF  		// 1111 1110 1111 1111 = 'a' 
	teq 	r0, r1 				// if 'a' pressed 
	beq 	StartMenuAPressed 	// v

	ldr 	r1, 0xFFEF 			// 1111 1111 1110 1111 = 'up'
	teq 	r0, r1  			// if 'up' pressed 
	beq 	StartMenuToggle 	// change position of start pointer 

	ldr 	r1, 0xFFDF 			// 1111 1111 1101 1111 = 'down'
	teq 	r0, r1 				// if 'down' pressed 
	beq 	StartMenuToggle 	// change position of start pointer 

	b 		readStartInput 			// else, reloop 


StartMenuAPressed: 					// 'A' is pressed on 'start game'
	ldr 	r0, =startSelState  	// load address of start selection state *** in GameState.s 
	ldr 	r1, [r0] 				

	cmp	 	r1, #0 					// if r1 != 0 quit game 
	bne 	StartExitGame 			// quit game 

	mov 	r0, #0  				// else, return 0 
	b 		startMenuDone 				// pop 


StartMenuToggle:
	

StartExitGame:
	bl 		clearScreen 			// clear screen *** make method to clear screen 
	mov 	r0, #1 					// return 1 
	b 		startMenuDone 

startMenuDone:
	pop 	{pc} 
