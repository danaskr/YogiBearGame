# YogiBear Game

**Author:** Dana Saker  
**Assignment:** Assignment 3 – Documentation

## Game Overview

Yogi Bear wants to collect all the picnic baskets in the forest of Yellowstone National Park. This park contains mountains and trees that serve as obstacles for Yogi. Besides the obstacles, there are rangers who make it harder for Yogi to collect the baskets.

### Game Rules

- **Rangers Movement:** Rangers can move only horizontally or vertically in the park
- **Life System:** If a ranger gets too close (one unit distance) to Yogi, then Yogi loses one life
- **Respawn System:** If Yogi still has at least one life from the original three, then he spawns at the entrance of the park
- **Scoring:** The game counts the number of picnic baskets that Yogi collected
- **Level Progression:** If all baskets are collected, a new game level is loaded or generated
- **Game Over:** If Yogi loses all his lives, a popup messagebox appears where the player can type their name and save it to the database

### Menu Features

- **Highscore Table:** Displays the top 10 best scores with player names
- **Game Restart:** Menu item to restart the game

## Task Analysis

The game flow follows this structure:

1. **MainMenu:** User must input a name to proceed
2. **Level Definition:** Each level is defined once per game (per player) and updated through level text files
3. **Game Engine:** Defines all keyboard input and assigns tasks accordingly
4. **Game Completion:** Based on user selection, the game finishes once they agree not to move to the next level (unless they lose the current level)
5. **Data Storage:** The game only stores player data when they win
6. **Post-Game:** Leaderboard is shown with options to start a new game or play as another player

## Technical Implementation

### ActionListener Implementation

#### MainMenu
- Button action checks if user has inputted a name and continues accordingly

#### Game Engine Class
- All possible keys and values are defined in a HashMap
- Each key in the HashMap is assigned movement functionality for the game object (the bear)
- Movement includes Up/Down/Left/Right with applied restrictions based on surrounding objects

#### Leaderboard Class
- New game action listener identifies a new game and removes all screen components before proceeding

### Key Features

#### Level Processing
- Level class processes level text files based on player willingness to continue
- Text file is processed line by line to build the map
- Before bear movement, collision detection prevents walking through non-walkable objects

#### Ranger System
- Rangers are defined based on text file parameters
- Two ranger types defined using x and y variables for movement description
- Bear has original starting position that remains walkable when bear is not present
- Collision with ranger results in life loss and respawn at stored starting position

## Test Cases

### Movement Tests
- **Start Game:** Verify Yogi Bear spawns at park entrance
- **Free Movement:** Verify Yogi Bear can move freely without obstacle collision
- **Obstacle Collision:** Verify Yogi Bear cannot move through trees or mountains
- **Respawn Location:** Verify Yogi Bear respawns at same entrance location after losing a life

### Gameplay Tests
- **Basket Collection:** Verify basket count increments correctly when collected
- **Ranger Collision:** Verify Yogi Bear loses one life and respawns when hitting a ranger
- **Multiple Ranger Collisions:** Verify life loss for each ranger collision
- **Level Completion:** Verify new level loads when all baskets are collected

### Game Over Tests
- **Life Depletion:** Verify popup appears for name entry when all lives are lost
- **Score Saving:** Verify score saves to database with valid player name
- **Invalid Name Handling:** Verify game rejects invalid player names
- **Database Error Handling:** Verify graceful error handling during database failures

### Interface Tests
- **Game Restart:** Verify game restarts from beginning
- **Highscore Display:** Verify table shows top 10 scores with player names
- **Score Accuracy:** Verify highscore table reflects accurate top 10 scores across multiple games
- **Keyboard Controls:** Verify game is fully playable using only keyboard (accessibility)
