////////////////////////////////////////////////////////////////////////////////////
//
// Collision Detection
//
// Luigi's move is in r1
// Collision object is in r2
//
// TODO align to some value,
// TODO fix error:
// 	source/CollisionHandler.s:154: Error: garbage following instruction -- `mov r1,#0B0001q'
//
////////////////////////////////////////////////////////////////////////////////////

.globl CollisionHandler
.globl killLuigi


// Codes for objects:

// MarioTemp 		00000
// Mario 		00001
// Goomba 		00010
// Ghost 		00011
// Coin Box 		00100
// Wood Block #1 	00101
// Wood Block #2 	00110
// Wood Block #3 	00111
// Pipe 		01000
// Hole  		01001


CollisionHandler:
		// TODO: Need to write CMP for every object... Goomba,Ghost, Coin Box, etc.

		// @Olga: Great work on the collision detection! I am not sure if we can
		// check for the collision object like this though. This assumes that
		// the collision object code is given as input in r2. But it is not yet known


		PUSH    {r4-r10, lr}

		CMP	r2, #0b00010			// Number codes for objects
		Beq	goombaHandler

		CMP	r2, #0b00011
		Beq	ghostHandler

		CMP	r2, #0b00100
		Beq	coinBoxHandler

		CMP 	r2, #0b01101
		Beq	woodBlockHandler

		CMP	r2, #0b01000
		Beq	pipeHandler

		CMP	r2, #0b010001
		Beq	holeHandler


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
