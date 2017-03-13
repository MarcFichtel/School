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
state:
        .int    0       // Will hold a pointer to the game map
        
        .int    0       // Position X of Mario
        .int    0       // Position Y of Mario
        
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
