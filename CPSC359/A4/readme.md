## Global Functions
* basicShapes.s:      DrawSquare(X, Y, Color) --> Draws 64x64 squares
* basicShapes.s:      DrawVertLine(X, Y, Ybound, Color)
* basicShapes.s:      DrawHorizLine(X, Y, Xbound, Color)
* contoller.s:        InitSES()
* contoller.s:        ReadData()
* contoller.s:        WriteClock(Value)
* contoller.s:        WriteLatch(Value)
* contoller.s:        ReadSNES()
* contoller.s:        Wait(Value)
* framebuffer.s:      InitFrameBuffer()
* framebuffer.s:      ClearScreen() --> Paints screen black
* framebuffer.s:      DrawSquare(X, Y, Size, Color)
* framebuffer.s:      DrawPixel(X, Y, Color)
* framebuffer.s:      DrawChar(X, Y, Character) --> Draws characters in white
* framebuffer.s:      DrawString(X, Y, String Address) --> Draws strings in white
* framebuffer.s:      DrawImage(X, Y, Image Address) --> Draws 64x64 images
* framebuffer.s:      DrawBG() 
* IntegerToString.s:  IntegerToString(Integer, String Address)
* IntegerToString.s:  DivideTwoNum(Dividend, Divisor)
* IntegerToString.s:  ModularDivision(Dividend, Divisor)
* IntegerToString.s:  FlipString(String Address)
* playerInput.s:      InGameMenuControl()
* playerInput.s:      MainMenuControl()
* playerInput.s:      PlayerControl()
* playerInput.s:      Delay()
* playerInput.s:      LongDelay()

# CPSC 359

## Assignment 4

The game environment is a finite 2D n x m grid (not necessarily a square). As Mario moves to the right or left, the view of the map will move accordingly loading new scenes to the game. You can load the new scenes to the game as Mario moves forward piece after piece or you can do it in such way that loads the whole new scene of the game (as Mario reaches the right edge of screen).

## Game Map
A game map is an instance of the game environment (for a value of 20 ≤ n ≤ 25 & m > 20). 
* Specifies the score of the player in terms of coins collected and general score.
* Specifies the number of lives left (starting with a minimum of 3 lives).
* There should be at least 3 different stages of the map which Mario will explore moving forward.
* The objects in the game are: Mario, monsters (min 1), coin blocks (min 3), wood blocks (min 2), green pipes (min 1), and holes (min 1).
* Mario can move left, right, and he can also jump
* The game should contain at least 2 different types of monsters.
* The coin blocks are in midair, Mario has to jump to be able to get the coins. On hit, the block will remain there but with no coin value.
* The wood blocks are usually located next to the coin blocks. If Mario jumps and hits a wood block from the bottom, then this block will be destroyed. Mario can jump on top of these blocks and walk on top of the wood and coin blocks.
* The green pipes are just an obstacle in Mario’s way; he should jump on top of them to proceed.

## Game State
A game state is a representation of the game as it is being played, and contains:
* An instance of a game map.
* Mario and the different tiles positions on the game map (Initialized to a starting position).
* The score and coins collected by the player (initialized to zero).
* Number of lives left.
* A win condition flag. 
* A lose condition flag 

## Transitions
The game transitions into a new state when the player performs an action:
* Mario jumps into a coin block gaining a coin and score value
* Mario jumps and hits the top of the monster which removes the monster and increases the score
* Mario gets hit by a monster or falls off the map into a hole will make Mario lose an available life or set the lose flag
* Mario reaches the castle; this will set the win condition flag

## Value Packs
Value-Packs:
* You must implement at least one value-pack of your choice that adds some feature to the game (such as a pack to giving life to Mario or speeding up the game etc.).
* You may get extra marks for additional creative value-packs.
* The value packs should appear after about 30 seconds into the game (to do so, you should use interrupts on the time) in a random place. Check https://en.wikipedia.org/wiki/Xorshift for a simple random number generator.

## Action
Action: 
* Move by one cell in one of the up, left and right directions if the action is valid. The move results in Mario’s position being set to the position of the destination cell.
* Moving into a value-pack tile will result in the overall score being incremented, the removal of the value-pack marking on the destination tile, and the application of the feature effect.

## Game End
Game ends when:
* Mario gets inside the castle (win).
* Mario’s lives become zero (lose).
* The user decides to quit. The game is over when either the win or lose condition flags is set in the game state 

## Main Menu Screen
* The Main Menu interface is drawn to the screen
* Game title is drawn somewhere on the screen
* Creator name(s) drawn somewhere on the screen
* Menu options labeled “Start Game” and “Quit Game”
* A visual indicator of which option is currently selected

## Menu Interaction
The player uses the SNES controller to interact with the menu
* Select between options using Up and Down on the D-Pad
* Activate a menu item using the A button
* Activating Start Game will transition to the Game Screen
* Activating Quit Game will clear the screen and exit the game 

## Game Screen
The current game state is drawn to the screen
* Represented as a 2D grid of cells
* All cells in the current game state are drawn to the screen
* Each cell is at least 32x32 pixels
* 2D grid should be (roughly) in the center of the screen
* Different tile types are drawn with a different visual representation (ie: Mario, pipes, floor, monsters, coins, wood blocks, etc...). Minimally, in color and shape.
* Score and lives left are also drawn on the screen (A label followed by the decimal value for each field)
* If the “Win Condition” flag is set, display a “Game Won” message
* If the “Lose Condition” flag is set, display a “Game Lost” message (Both messages should be prominent (ie, large, middle of screen)) 

## Game Interaction
The player uses the SNES controller to interact with the game
* Pressing up, left or right on the D-Pad will attempt a move action 
* Pressing the Start button will open a Game Menu (With two menu items: Restart Game and Quit). 
* Visually display menu option labels and a selector (Menu drawn on a filled box with a border in the center of the screen)
* Normal game controls not processed when Game Menu is open (Pressing Start will close the Game Menu)
* Press up and down on D-Pad to select between menu options (Pressing the A button activates a menu option)
* Activating Restart Game will reset the game to its original state 
* Activating Quit will transition to the Main Menu screen 
* If the win condition or lose condition flags are set, pressing any button will return to the Main Menu screen.
