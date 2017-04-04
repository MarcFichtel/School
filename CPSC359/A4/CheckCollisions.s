////////////////////////////////////////////////////////////////////////////////////
//
// Collision Detection
// 		Changes state of collision flags when luigi encounters different block types.
//
// TODO fix error:
// 	source/CollisionHandler.s:154: Error: garbage following instruction -- `mov r1,#0B0001q'
// 		solution: align to some value, .4?
////////////////////////////////////////////////////////////////////////////////////

.align 4 // this might have fixed todo on line 9

.globl CheckCollisions
CheckCollisions:

		// name registers
		pX 				.req r4
		pY				.req r5
		set 			.req r6
		tempcoor 	.req r7
		tempxy 		.req r8
		clear 		.req r9

	 	MOV		 		set, #1 						// initilize set to 1
		MOV 			clear, #0 					// clear = 0
		LDR 			r0, =state 					// load flags address
		LDR 			pX, [r0, #4]				// luigi x
		LDR 			pY, [r0, #8] 				// luigi y

	checkVerticalCollision: 				// check vertical axis collisions
		LDR 	r1, [r0, #20] 		// load jump flag into arbitrary register
		CMP 	r1, set 					// if jump flag is set...
		Beq 	checkUpCollision 		// check upper block

	checkDownCollision: 			// else check lower block
		CMP 	py, #640 					// no holes to check in scene1, just check if player is above the whole floor
		Bge 	setDownFlag 			// if y is greater or equal to 640, set flag to true
		B 		clearDownCol 			// else clear collision flag (takes care of falling case)

	setDownFlag:
		LDR 	r0, =state
		STR 	set, [r0, #52]
		B checkHorizontalCollision 	// branch to check horizontal

	clearDownCol:
			LDR 	r0, =state
			STR 	clear, [r0, #52]
			B checkHorizontalCollision 	// branch to check horizon

	checkUpCollision:
			LDR 	r0, =map11blocks 				// load address of map 1 scene 1 *** change this later to work with other scenes
			MOV 	tempcoor, #4 						// initilize index for block array

		loopcheckup:
			LDR		tempxy, [r0, tempcoor] 	// load a block
			SUB  	tempxy, #64  						// [(block y) - 64] to test if luigi is under block
			CMP 	py, tempxy 							// if px == blockx...
			Beq 	setUpperColFlag 				// set right collision flag

			ADD 	tempcoor, #8 						// tempcoor ++
			CMP 	tempcoor, #56 					// if temp < # of blocks' x coors...
			Blt 	loopcheckright 					// continue loop
			B			clearUpCol 							// clear upper collision flag if no collision

	setUpperColFlag:
			LDR 	r0, =state
			STR 	set, [r0, #48]
			B 		checkHorizontalCollision 	// branch to check horizontal

	clearUpCol:
			LDR 	r0, =state
			STR 	clear, [r0, #48]
			B 		checkHorizontalCollision 	// branch to check horizontal

			// check horizontal cases
	checkHorizontalCollision:
		LDR 	r1, [r0, #36] 		// Load luigi moving right flag
		CMP 	r1, set 					// if player moving right
		Beq 	checkRightCollision

		// check if walking left
		LDR 	r1, [r0, #32] 		// Load luigi moving left flag
		CMP 	r1, set 					// if player moving left
		Beq 	checkLeftCollision 		// go to check left collision
		B 		collisioncheckdone  	// else finish function (all cases checked: up down right left)

		checkRightCollision:
				LDR 	r0, =map11blocks 				// load address of map 1 scene 1 *** change this later to work with other scenes
				MOV 	tempcoor, #0 						// initilize index for block array
			loopcheckright:
				LDR		tempxy, [r0, tempcoor] 	// load a block
				SUB 	tempxy, #64  						// [(block x) - 64] to test if luigi is  right of block
				CMP 	px, tempxy 							// if px == blockx...
				Beq 	setRightColFlag 				// set right collision flag
				ADD 	tempcoor, #8 						// tempcoor ++
				CMP 	tempcoor, #60 					// if temp < # of blocks' x coors...
				Blt 	loopcheckright 					// continue loop
				B 		clearRightColFlag 			// else finish function (right collision checked completely)

		checkLeftCollision:
				LDR 	r0, =map11blocks 				// load address of map 1 scene 1 *** change this later to work with other scenes
				MOV 	tempcoor, #0 						// initilize index for block array

			loopcheckleft:
				LDR		tempxy, [r0, tempcoor] 	// load a block
				ADD  	tempxy, #64  						// [(block x) + 64] to test if luigi is left of block
				CMP 	px, tempxy 							// if px == blockx...
				Beq 	setLeftColFlag 					// set left collision flag
				ADD 	tempcoor, #8 						// tempcoor ++
				CMP 	tempcoor, #60 					// if temp < # of blocks' x coors...
				Blt 	loopcheckleft 					// continue loop
				B			clearLeftColFlag 			// else finish function (left collision checked completely)

		setRightColFlag: 							// set right collision flag
				LDR 	r0, =state
				STR 	set, [r0, #36]
				B 		collisioncheckdone

		clearRightColFlag: 							// clear right collision flag
				LDR 	r0, =state
				STR 	clear, [r0, #36]
				B 		collisioncheckdone


		setLeftColFlag: 							// set left collision flag
				LDR 	r0, =state
				STR 	set, [r0, #44]
				B 		collisioncheckdone

		clearLeftColFlag: 							// clear left collision flag
				LDR 	r0, =state
				STR 	clear, [r0, #44]
				B 		collisioncheckdone

		collisioncheckdone:
				POP    {r4-r10, pc}


////////////////////////////////////////////////////////////////////////////////////
//
//   GOOMBA
//
////////////////////////////////////////////////////////////////////////////////////

goombaHandler:

		MOV 	r4, r1				// Moves Luigi into r4
		MOV 	r1, r2				// Moves Goomba code (0b00010) into r1
		BL 	objLocation			// Calls objLocation from Object Location class to get Luigi's location

		LDR 	r5, [r0], #4
		LDR 	r6, [r0]			// Load in top left of goomba

		MOV 	r2, #0B00001			// Luigi's object code in r2
		BL 	objLocation
		LDR 	r9, [r0, #4]

		MOV 	r2, #0B00000			// Temporary Luigi in r2
		BL 	objLocation
		LDR 	r7, [r0, #4]


		CMP     r7, r9				// If Luigi touches goomba from the side, kill Luigi
		Beq     killLuigi

		CMP     r7, r9				// If Luigi touches goomba from the top, kill goomba
		Bgt     killGoomba

		B killLuigi

killGoomba:
		MOV 	r1, #0B00010
		BL 	objLocation
        	MOV     r11, r0

		LDR	r0, [r11], #4
		LDR	r1, [r11], #20
		LDR 	r2, [r11], #4
		LDR 	r3, [r11]

		// TODO LDR 	r4, =image // ******>>>>background image to replace goomba
		BL CreateImage				// Erases goomba from the frame (replaces the image)

		MOV 	r1, #0B00010
		BL objLocation
		MOV     r11,    r0
		LDR     r0,     =5000
		MOV 	r1, #50
		LDR     r2,     =5031
		MOV 	r3, #19

		STR 	r0, [r11], #4
		STR 	r0, [r11], #4
		STR 	r3, [r11], #4
		STR 	r0, [r11], #4
		STR 	r0, [r11], #4
		STR 	r3, [r11], #4
		STR 	r3, [r11], #4
		STR 	r3, [r11], #4

		B incrementScore

////////////////////////////////////////////////////////////////////////////////////
//
//   GHOST
//
////////////////////////////////////////////////////////////////////////////////////

ghostHandler:

		MOV 	r4, r1				// Moves Luigi into r4
		MOV 	r1, r2				// Moves collision object into r1
		BL 	objLocation			// Calls objLocation from Object Location class to get Luigi's location

		LDR 	r5, [r0], #4
		LDR 	r6, [r0]			// Load in top left of ghost

		MOV 	r2, #0B00001			// Luigi's object code in r2
		BL 	objLocation
		LDR 	r9, [r0, #4]

		MOV 	r2, #0B00000			// Temporary Luigi in r2
		BL 	objLocation
		LDR 	r7, [r0, #4]


		CMP     r7, r9				// If Luigi touches ghost from the side, kill Luigi
		Beq     killLuigi

		CMP     r7, r9				// If Luigi touches ghost from the top, kill ghost
		Bgt     killGoomba

		B killLuigi

killGhost:
		MOV 	r1, #0B00011
		BL 	objLocation
        	MOV     r11, r0

		LDR	r0, [r11], #4
		LDR	r1, [r11], #20
		LDR 	r2, [r11], #4
		LDR 	r3, [r11]

		// TODO LDR 	r4, =image // ******>>>>background image to replace ghost
		BL CreateImage				// Erases ghost from the frame (replaces the image)

		MOV 	r1, #0B00011
		BL objLocation
		MOV     r11,    r0
		LDR     r0,     =5000
		MOV 	r1, #50
		LDR     r2,     =5031
		MOV 	r3, #19

		STR 	r0, [r11], #4
		STR 	r0, [r11], #4
		STR 	r3, [r11], #4
		STR 	r0, [r11], #4
		STR 	r0, [r11], #4
		STR 	r3, [r11], #4
		STR 	r3, [r11], #4
		STR 	r3, [r11], #4

		B incrementScore


////////////////////////////////////////////////////////////////////////////////////
//
//   COIN BOX
//
////////////////////////////////////////////////////////////////////////////////////


coinBoxHandler:

        	MOV 	r1, r2

		MOV 	r1, #1
		BL objLocation

		LDR 	r9, [r0], #4
		LDR 	r10, [r0]			// Load top left point of Luigi's location

		MOV 	r1, #0B00000
		BL objLocation

		LDR 	r7, [r0]
		LDR 	r8, [r0, #4]			// Load top left point of Temp Luigi's location

		CMP 	r8, r10
		Bge exit

        	MOV     r2, #1				// Increment coin count by 1

		B incrementCoin


////////////////////////////////////////////////////////////////////////////////////
//
//   WOOD BLOCK
//
////////////////////////////////////////////////////////////////////////////////////

woodBlockHandler:

       		MOV 	r1, r2

		MOV 	r1, #1
		BL objLocation

		LDR 	r9, [r0], #4
		LDR 	r10, [r0]			// Load top left point of Luigi's location

		MOV 	r1, #0B00000
		BL objLocation

		LDR 	r7, [r0]
		LDR 	r8, [r0,#4]			// Load top left point of Temp Luigi's location

		CMP 	r8, r10
		Bge exit


breakWoodBlock:

		MOV 	r5, r4
		MOV 	r1, r2
		BL objLocation
        	MOV     r11,    r0

		LDR 	r0, [r11], #4
		LDR	r1, [r11], #20
		LDR 	r2, [r11], #4
		LDR 	r3, [r11]

		// TODO LDR 	r4, =sky
		BL CreateImage				// Erases wood block from the frame (replaces the image)

		MOV 	r1, r5
		BL objLocation
        	MOV     r11,    r0

		LDR     r0,     =5000
		MOV 	r1, #50
		LDR     r2,     =5031
		MOV 	r3, #19

		STR 	r0, [r11], #4
		STR 	r0, [r11], #4
		STR 	r3, [r11], #4
		STR 	r0, [r11], #4
		STR 	r0, [r11], #4
		STR	r3, [r11], #4
		STR 	r3, [r11], #4
		STR 	r3, [r11], #4

		B exit

////////////////////////////////////////////////////////////////////////////////////
//
//   PIPE
//
////////////////////////////////////////////////////////////////////////////////////

pipeHandler:						// no collision with the pipe
		B exit


////////////////////////////////////////////////////////////////////////////////////
//
//   HOLE
//
////////////////////////////////////////////////////////////////////////////////////


holeHandler:

		MOV 	r1, r2
		BL objLocation

		LDR 	r5, [r0]
		LDR	r6, [r0, #4]			// Load top left point of the hole
		LDR 	r9, [r0, #8]
		LDR	r10, [r0, #12]			// Load top right point of the hole


		MOV 	r1, #0B00000
		BL objLocation

		LDR 	r7, [r0, #16]
		LDR 	r8, [r0, #20]			// Load Bottom left point of temp Luigi
		LDR 	r11, [r0, #24]
		LDR 	r12, [r0, #28]			// Load Bottom right point of temp Luigi

		CMP 	r5, r7
		Blt exit

		CMP 	r9, r11
		Bgt exit

		B killLuigi

////////////////////////////////////////////////////////////////////////////////////
//
//   KILL MARIO
//
////////////////////////////////////////////////////////////////////////////////////

killLuigi:

		MOV r5, #0B00001
		MOV r1, r5
		BL objLocation
	        MOV     r11,    r0

		LDR r0, [r11], #4
		LDR r1, [r11], #20
		LDR r2, [r11], #4
		LDR r3, [r11]

		// TODO LDR r4, =sky				// FIX the name here to replace Luigi with backgroung image when he dies
		BL CreateImage

		MOV r1, r5
		BL objLocation
        	MOV     r11,    r0

		LDR     r0,     =5000
		MOV r1, #50
		LDR     r2,     =5031
		MOV r3, #19
		STR r0, [r11], #4
		STR r1, [r11], #4
		STR r2, [r11], #4
		STR r3, [r11], #4
		STR r0, [r11], #4
		STR r1, [r11], #4
		STR r2, [r11], #4
		STR r3, [r11], #4

		// TODO LDR r0, =Life
		LDR r1, [r0]
		suB r1, r1, #1
		STR r1, [r0]

		B NewLife				// Calls the function to start a new life if there are any left


incrementScore:

		// TODO LDR r0, =Score				// FIX the name
		LDR r1, [r0]
		ADD r1, r1, #1
		STR r1, [r0]

        	BL updateScore				// Branch to update score !!! FIX the name

        	B       exit

incrementCoin:

		// TODO LDR r0, =Coins				// FIX the name
		LDR r1, [r0]
		ADD r1, r1, r2
		STR r1, [r0]

        	BL updateCoin				// Branch to update coin !!! FIX the name

        	B       exit

////////////////////////////////////////////////////////////////////////////////////
//
//   EXIT
//
////////////////////////////////////////////////////////////////////////////////////


exit:
      		POP     {r4-r10, pc}

////////////////////////////////////////////////////////////////////////////////////
//
//   Get Object Location
//
////////////////////////////////////////////////////////////////////////////////////

.globl objLocation
objLocation:
	PUSH {lr}

	POP {pc}

////////////////////////////////////////////////////////////////////////////////////
//
//   Create Image
//
////////////////////////////////////////////////////////////////////////////////////

.globl CreateImage
CreateImage:
	PUSH {lr}

	POP {pc}

////////////////////////////////////////////////////////////////////////////////////
//
//   New life
//
////////////////////////////////////////////////////////////////////////////////////

.globl NewLife
NewLife:
	PUSH {lr}

	POP {pc}

////////////////////////////////////////////////////////////////////////////////////
//
//   Update Score
//
////////////////////////////////////////////////////////////////////////////////////

.globl updateScore
updateScore:
	PUSH {lr}

	POP {pc}

////////////////////////////////////////////////////////////////////////////////////
//
//   Update Coin
//
////////////////////////////////////////////////////////////////////////////////////

.globl updateCoin
updateCoin:
	PUSH {lr}

	POP {pc}
