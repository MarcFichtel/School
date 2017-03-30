//////////////////////////////////////////////////////////////////////////////////////
// Game state stuff
//////////////////////////////////////////////////////////////////////////////////////

.section .text

.global GameState

// TODO
// ~ Set initial positions of game objects
// ~ Decide which functions should be included in this file (i.e. stuff that relates to the game state)

//////////////////////////////////////////////////////////////////////////////////////
// Initialize the game state
//////////////////////////////////////////////////////////////////////////////////////

GameState:



//////////////////////////////////////////////////////////////////////////////////////
// Data Section
//////////////////////////////////////////////////////////////////////////////////////

.section .data

.align 4
.globl state
state:
        .int    0       // Will hold a pointer to the game map

        .int    0       // Position X of player character
        .int    0       // Position Y of player character
	.int 	0 	// PC anim state (0 when idle, 2 when jumping, 0/1 when running)

        .int    0       // Wooden block 1 position X
        .int    0       // Wooden block 1 position Y

        .int    0       // Coin block 1 position X
        .int    0       // Coin block 1 position Y

        .int    0       // Pipe 1 position X
        .int    0       // Pipe 1 position Y

        .int    0       // Score
        .int    0       // Coins collected
        .int    3       // Lives left

        .int    0       // Win flag
        .int    0       // Lose flag

.globl authorsString
authorsString: 	.asciz 	"Olga Bogdanova | Cardin Chen | Marc-Andre Fichtel"

