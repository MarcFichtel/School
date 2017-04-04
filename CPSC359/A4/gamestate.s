//////////////////////////////////////////////////////////////////////////////////////
// Game state stuff
//////////////////////////////////////////////////////////////////////////////////////

.section .text

//////////////////////////////////////////////////////////////////////////////////////
// Reset the game state
// TODO Add stats for enemies, coins, etc
//////////////////////////////////////////////////////////////////////////////////////

.globl ResetGameState 			// Make function global
ResetGameState:
	PUSH 	{r4-r6, lr} 		// Start function

	// Setup
	MOV 	r1, #0 			// r1 = 0
	MOV 	r2, #64			// r2 = 64 (default player position X)
	MOV 	r3, #576 		// r3 = 704 (default player position Y)
	MOV 	r4, #1 			// r4 = 1 (default collision down)
	MOV 	r5, #3 			// r5 = 3 (default lives)
	MOV 	r6, #20 		// r6 = 20 (default move & jump speed)
	LDR 	r0, =state 		// Load state address

	// Restore default game state
	STR 	r1, [r0] 		// Game map = 0 // todo - update game map to 1,2,3 at end of map1_1
	STR 	r2, [r0, #4] 		// Player X = 64
	STR 	r3, [r0, #8] 		// Player Y = 576
	STR 	r6, [r0, #12] 		// Move Speed (default = 20)
	STR 	r6, [r0, #16] 		// Jump Speed (default = 20)
	STR 	r1, [r0, #20] 		// Jumping = 0 (false)
	STR 	r1, [r0, #24] 		// Falling = 0 (false)
	STR 	r1, [r0, #28] 		// Max Jump Height = 0
	STR 	r1, [r0, #32] 		// Move left = 0 (false)
	STR 	r1, [r0, #36] 		// Move right = 0 (false)

	STR 	r1, [r0, #40] 		// Collision right = 0 (false)
	STR 	r1, [r0, #44] 		// Collision left = 0 (false)
	STR 	r1, [r0, #48] 		// Collision up = 0 (false)
	STR 	r4, [r0, #52] 		// Collision down = 1 (true)

	STR 	r1, [r0, #56] 		// Score = 0
	STR 	r1, [r0, #60] 		// Coins collected = 0
	STR 	r5, [r0, #64] 		// Lives left = 3
	STR 	r1, [r0, #68] 		// Win = 0 (false)
	STR 	r1, [r0, #72] 		// Lose = 0 (false)
	STR 	r1, [r0, #76] 		// Walk animation flag = 0 (luigi1, idle)

	POP 	{r4-r6, pc} 		// End function

//////////////////////////////////////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////////////////////////////////////

.section .data

.align 4
.globl state
state:
  .int    0       // 00 - Will hold a pointer to the game map // todo - update game map to 1,2,3 at end of map1_1
  .int    64     	// 04 - Position X of player character
  .int    576    	// 08 - Position Y of player character
	.int 	0 	// 12 - Move Speed
	.int 	0 	// 16 - Jump Speed
	.int 	0 	// 20 - Jumping up flag (is player currently jumping up?)
	.int 	0 	// 24 - Falling flag (is the player currently falling?)
	.int 	0 	// 28 - Max Jump height
	.int 	0 	// 32 - Moving left flag (is player currently moving left?)
	.int 	0 	// 36 - Moving right flag (is player currently moving right flag?)

	.int 	0 	// 40 - Move right blocked (used for collision detection)
	.int 	0 	// 44 - Move left blocked (used for collision detection)
	.int 	0 	// 48 - Move up blocked (used for collision detection)
	.int 	1 	// 52 - Move down blocked (used for collision detection)

  .int    0       // 56 - Score
  .int    0       // 60 - Coins collected
  .int    3       // 64 - Lives left
  .int    0       // 68 - Win flag
  .int    0       // 72 - Lose flag

	.int 	0 	// 76 - Walk animation flag (0 = luigi1, 1 = luigi2)

	.int 	0		// 80 TODO Goomba move speed
	.int 	0 	// 84 TODO Goomba position X
	.int 	0 	// 88 TODO Goomba position Y
	.int 	0 	// 92 TODO Goomba walk animation flag
	.int 	0 	// 96 TODO Goomba dead flag

	.int 	0	// 100 TODO Boo move speed
	.int 	0 	// 104 TODO Boo position X
	.int 	0 	// 108 TODO Boo position Y

	.int 	0 	// 112 TODO Coin 1 position X
	.int 	0 	// 116 TODO Coin 1 position Y
	.int 	0 	// 120 TODO Coin 1 collected flag
	.int 	0 	// 124 TODO Coin 2 position X
	.int 	0 	// 128 TODO Coin 2 position Y
	.int 	0 	// 132 TODO Coin 2 collected flag
	.int 	0 	// 136 TODO Coin 3 position X
	.int 	0 	// 140 TODO Coin 3 position Y
	.int 	0 	// 144 TODO Coin 3 collected flag
	.int 	0 	// 148 TODO Coin 4 position X
	.int 	0 	// 152 TODO Coin 4 position Y
	.int 	0 	// 156 TODO Coin 4 collected flag
	.int 	0 	// 160 TODO Coin 5 position X
	.int 	0 	// 164 TODO Coin 5 position Y
	.int 	0 	// 168 TODO Coin 5 collected flag

	// TODO Add stats for various blocks, winning goal flag, castle, etc

.globl 	map11blocks
map11blocks:
	.int  256 	// 0 brick 1 x
	.int  384 	// 4 brick 1 y
	.int  512 	// 8 brick 2 x
	.int  384 	// 12 brick 2 y
	.int  640 	// 16 brick 3 x
	.int  384 	// 20 brick 3 y
	.int  768 	// 24 brick 4 x
	.int  384 	// 28 brick 4 y
	.int  576 	// 32 coinblock 1 x
	.int  384 	// 36 coinblock 1 y
	.int 	704 	// 40 coinblock 2 x
	.int  384		// 44 coinblock 2 y
	.int  640 	// 48 coinblock 3 x
	.int 	128 	// 52 coinblock 3 y
	.int 	0 		// 56 floor x
	.int 	640 	// 60 floor y
	.int 	1024 	// 64 floor length
	.int 	960 	// 68 initial goomba x
	.int 	640 	// 72 initial goomba y 

.globl authorsString
authorsString: 	.asciz 	"Olga Bogdanova | Cardin Chen | Marc-Andre Fichtel"
