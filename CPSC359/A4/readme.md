# CPSC 359

## Assignment 4
Due ___

### TODOs
* Visuals
  - Background
    ~ Render
    ~ Move with camera
  - Player & Non-Player Characters
    ~ Render
    ~ Animate
    ~ Create animation state controllers (i.e. Player: Idle, Running, Jumping, Ducking, Dying)
  - Menu Screen, Game Over Screen, Win Screen, In-Game UI
    ~ Render
    ~ Update stats (Time, Score, Lives, etc)
* Controls
  - Menu
    ~ Get & Process user input (i.e. click start, no 2-Player option)
  - Game Over
    ~ Get & Process user input (return to menu, retry level)
  - In-Game
    ~ Get & Process user input (jump, run)
* AI
  - Model Enemy Behavior
    ~ Goomba (walks left)
    ~ Koopa Troopa
    ~ ???
* Game State
  - Track, display, and constantly update inbformation
    ~ Time
    ~ Score
    ~ Lives
    ~ ???
* Game Logic
  - Collision Detection
    ~ Player to Ground, Items
    ~ Player to Enemy (player gets hurt, enemy gets hurt)
    ~ Enemies / Items to Ground
    ~ Player to end flag pole
  - Physics
    ~ Run speed
    ~ Jump Speed
    ~ Acceleration
    ~ Jump height at idle/walk vs run
