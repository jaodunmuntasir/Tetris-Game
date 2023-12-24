Tetris Game


Written and Implemented By
MD JAODUN MUNTASIR (BVLLR5) 
Course: Programming Technology 
Faculty of Informatics
Eötvös Loránd University
Date: 21 DEC 2023
________________


Task


The Tetris game is a classic puzzle game where players strategically rotate, move, and drop a series of differently shaped blocks, known as "Tetriminos," onto a playing field. The objective is to complete horizontal lines of blocks without any gaps, which causes those lines to disappear and awards points to the player. The game increases in speed and complexity as the player progresses, adding to the challenge.


Key features of this Tetris game include:


* Progressive Levels: Players start at level one and advance to higher levels by surviving for a set duration without reaching a game over state. Each level increases in difficulty, typically by accelerating the speed at which Tetriminos fall.


* Scoring System: The game features a scoring mechanism where players earn points for each line they complete. The score accumulates across levels, encouraging players to maximize their score in each level.


* Game Controls: Players have the ability to rotate, move sideways, and speed up the descent of the Tetriminos, giving them control over how to place the blocks and strategize their gameplay.


* Pause and Restart Options: The game includes functionalities to pause and restart, enhancing the user experience by allowing players to take breaks or start a new game at their convenience.


* Game Over and Level Completion Mechanics: The game ends when the Tetriminos stack up to the top of the playing field, and it features a level completion mechanism with a congratulatory message and the option to proceed to the next level.


This implementation of Tetris offers an engaging and nostalgic gaming experience, challenging players with its puzzle-solving gameplay and rewarding strategic planning and quick reflexes.
________________


UML Diagram
  
________________

Description of the Methods
Board.java


* Constructor (public Board(Tetris parent)): Initializes the game board. It sets up key components like the timer for game cycles and the level timer for tracking progress through levels.
* initBoard(Tetris parent): Sets up the board with focus and key listeners and initializes the status bar.
* squareWidth() and squareHeight(): Calculate the width and height of each square in the game grid based on the board's dimensions.
* shapeAt(int x, int y): Determines the shape at a specific grid position.
* start(): Begins the game by initializing pieces, the game board, and starting the game cycle timer.
* pause(): Toggles the game's pause state and updates the status bar accordingly.
* paintComponent(Graphics g): Responsible for drawing game components on the board, including the Tetriminos.
* doDrawing(Graphics g): A helper method for paintComponent, it handles the rendering of shapes on the board.
* completeLevel(): Triggered when a level is completed, it stops the level timer, shows a completion message, and prepares for the next level.
* dropDown(): Manages the action of dropping a Tetrimino down in the grid.
* oneLineDown(): Moves the current piece one line down if possible.
* clearBoard(): Clears the board, setting all its cells to NoShape.
* pieceDropped(): Handles the logic when a piece is dropped, including placing it on the board and checking for completed lines.
* setPlayerName(String playerName): Sets the player's name for the game session.
* newPiece(): Generates a new Tetrimino piece and places it at the top of the board. It also adjusts game speed based on the level.
* boardIsEmpty(): Checks if the board is empty, typically used to determine the start of a new level.
* tryMove(Shape newPiece, int newX, int newY): Attempts to move the current piece to a new position.
* removeVerticalMatches(): Identifies and removes vertical matches of the same color on the board.
* updateScoreDisplay(): Updates the display of the current score.
* removeFullLines(): Checks for and removes fully completed lines from the board.
* drawSquare(Graphics g, int x, int y, Shape.Tetrominoe shape): Draws an individual square of a Tetrimino.
* Inner class GameCycle (implements ActionListener): Defines the actions to take in each cycle of the game, such as updating the game state.
* Inner class TAdapter (extends KeyAdapter): Handles keyboard input for controlling the Tetriminos.


Shape.java


* Constructor (public Shape()): Initializes a new shape instance. It sets up the shape's coordinates and default shape as NoShape.
* initShape(): Initializes the shape's coordinate array and the coordinate table, which defines the relative positions of blocks for each Tetrimino shape.
* getColor(): Returns the color associated with the current shape. This method also prints a message indicating the color of the created shape.
* setShape(Tetrominoe shape): Sets the shape of the Tetrimino to one of the predefined types (e.g., ZShape, SShape, LineShape, etc.). It updates the coordinates and color of the shape based on the shape type.
* setX(int index, int x) and setY(int index, int y): Utility methods to set the x and y coordinates of a block within the shape.
* x(int index) and y(int index): Returns the x or y coordinate of a block within the shape.
* getShape(): Returns the current shape type of the Tetrimino.
* setRandomShape(): Randomly selects one of the available Tetrimino shapes, excluding NoShape.
* minX() and minY(): Utility methods to find the minimum x and y coordinates among the blocks of the shape. These are used for bounding box calculations.
* rotateLeft() and rotateRight(): Returns a new Shape object representing the current shape rotated to the left or right. For square shapes, rotation has no effect, so the same shape is returned.


Tetris.java


* Constructor (public Tetris()): Initializes the game by displaying a start game dialog. If the player chooses to start, it sets up the user interface and starts the game; otherwise, it exits.
* showStartGameDialog(): Displays a dialog box at the beginning of the game, giving players the option to start or exit the game.
* initUI(): Sets up the user interface, including the game board, score display, level display, and control buttons (Quit, Restart, Pause). It also defines the layout and visual styling of these elements.
* getStatusBar(): Provides access to the status bar label, which displays the current score.
* getPlayerName(): Prompts the player to enter their name at the start of the game.
* gameOver(int score): Handles the game-over scenario by saving the player's score, disposing of the current game window, and showing the top scores.
* showTopScores(): Displays a new window showing the top scores in a table format, with options to restart the game or exit.
* confirmAndQuit(): Shows a confirmation dialog for quitting the game. If confirmed, it exits the application.
* confirmAndRestart(): Shows a confirmation dialog for restarting the game. If confirmed, it restarts the game.
* **restartGame()**: Restarts the game by disposing of the current window and launching a new instance of the Tetris game.
* Main method (public static void main(String[] args)): The entry point of the application. It schedules the creation and showing of the game UI in the event dispatch thread.




Scoreboard.java


* Constructor (public Scoreboard()): Initializes the scoreboard. It calls initializeDatabase to set up the database for storing scores.
* initializeDatabase(): Establishes a connection to the SQLite database and creates a table for scores if it doesn't already exist. The table includes columns for the player's name and score.
* addScore(String name, int score): Inserts a new score entry into the database. It takes the player's name and their score as parameters.
* getTopScores(int topN): Retrieves the top N scores from the database, ordered in descending order. It returns a list of ScoreEntry objects, each representing a high score entry.
* Inner class ScoreEntry: Represents a single score entry with two properties: name (the player's name) and score (the player's score). It provides getter methods to access these properties.








________________


Testing
* Game Initialization:
   * Test whether the game starts correctly upon user confirmation.
   * Verify that the start game dialog functions properly (both "Start Game" and "Exit" options).
  

  

* Gameplay Mechanics:
   * Verify that Tetriminos are generated randomly at the start of each game.
   * Check the rotation of Tetriminos (both left and right rotations) for all shapes.
   * Test the movement of Tetriminos (left, right, and downward movements).
   * Ensure the 'drop down' feature instantly places the Tetrimino at the bottom of the stack.
   * Confirm that Tetriminos lock in place upon reaching the bottom or stacking on another Tetrimino.
  





* Line Clearing and Scoring:
   * Test line clearing functionality when a line is fully occupied with blocks.
   * Validate score incrementation corresponding to the number of lines cleared.
   * Check if multiple line clears (e.g., double, triple, Tetris) are scored correctly.
  

* Level Advancement:
   * Ensure the game transitions to the next level after the set duration and that the level indicator updates correctly.
   * Verify increase in game speed with each new level.
  

* Game Over Conditions:
   * Test the game over condition when Tetriminos stack to the top of the board.
   * Check if the game over screen displays correctly with the final score.
  

* Pause and Resume Functionality:
   * Confirm that the game pauses and resumes correctly when the pause button is pressed.
  

* Restart Game:
   * Test the functionality of the restart button, ensuring the game restarts and score resets.
  

* Quit Game:
   * Verify the quit button and the confirmation dialog function as expected.
  

________________




















































References: https://github.com/janbodnar/Java-Tetris-Game/blob/master/LICENSE
