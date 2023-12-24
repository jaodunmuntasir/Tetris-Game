/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetrisgame;

/**
 *
 * @author Asus
 */
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class Board extends JPanel {

    private final int BOARD_WIDTH = 12;
    private final int BOARD_HEIGHT = 24;
    private final int PERIOD_INTERVAL = 300;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Shape curPiece;
    private Shape.Tetrominoe[] board;
    private Color[] boardColors;
    private Tetris parent;
    private String playerName;
    private int currentLevel = 1;
    private int levelScore = 0;
    private Timer levelTimer;

    public Board(Tetris parent) {

        initBoard(parent);
        
        // Set up the level timer
        levelTimer = new Timer(120000, e -> completeLevel());
        levelTimer.start();
    }

    private void initBoard(Tetris parent) {

        setFocusable(true);
        statusbar = parent.getStatusBar();
        addKeyListener(new TAdapter());
        this.parent = parent;
    }

    private int squareWidth() {

        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    private int squareHeight() {

        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private Shape.Tetrominoe shapeAt(int x, int y) {

        return board[(y * BOARD_WIDTH) + x];
    }

    void start() {

        curPiece = new Shape();
        board = new Shape.Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        boardColors = new Color[BOARD_WIDTH * BOARD_HEIGHT];

        clearBoard();
        
        // Initialize the timer before calling newPiece()
        timer = new Timer(PERIOD_INTERVAL, new GameCycle());
        timer.start();
        
        newPiece();
    }

    public void pause() {

        isPaused = !isPaused;

        if (isPaused) {

            statusbar.setText("paused");
        } else {

            statusbar.setText(String.valueOf(numLinesRemoved));
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {

            for (int j = 0; j < BOARD_WIDTH; j++) {

                Shape.Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != Shape.Tetrominoe.NoShape) {

                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getShape() != Shape.Tetrominoe.NoShape) {

            for (int i = 0; i < 4; i++) {

                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);

                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
    } 
    
    private void completeLevel() {
        // Stop the timer and show level completion dialog
        levelTimer.stop();
        JOptionPane.showMessageDialog(this, "Congratulations. You passed Level " + currentLevel + ".", "Level Complete", JOptionPane.INFORMATION_MESSAGE);

        // Prepare for the next level
        currentLevel++;
        levelScore = numLinesRemoved;
        clearBoard(); // Reset the board for the next level
        levelTimer.start(); // Start the timer for the next level
    }

    private void dropDown() {

        int newY = curY;

        while (newY > 0) {

            if (!tryMove(curPiece, curX, newY - 1)) {

                break;
            }

            newY--;
        }

        pieceDropped();
    }

    private void oneLineDown() {

        if (!tryMove(curPiece, curX, curY - 1)) {

            pieceDropped();
        }
    }

    private void clearBoard() {

        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {

            board[i] = Shape.Tetrominoe.NoShape;
        }
    }

    private void pieceDropped() {

        for (int i = 0; i < 4; i++) {

            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            int idx = (y * BOARD_WIDTH) + x;
            board[idx] = curPiece.getShape();
            boardColors[idx] = curPiece.getColor();
        }

        removeVerticalMatches();
        removeFullLines();

        if (!isFallingFinished) {

            newPiece();
        }
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private void newPiece() {
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();
    
        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Shape.Tetrominoe.NoShape);
            timer.stop();
            var msg = String.format("Game over.  Score: %d", numLinesRemoved);
            statusbar.setText(msg);
            parent.gameOver(numLinesRemoved);
            levelTimer.stop();
        }
        
        if (boardIsEmpty()) {
            numLinesRemoved = levelScore;
        }

        // Adjust the speed based on the level
        int newDelay = Math.max(PERIOD_INTERVAL - (10 * currentLevel), 100);
        timer.setDelay(newDelay);
    }

    private boolean boardIsEmpty() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] != Shape.Tetrominoe.NoShape) {
                return false;
            }
        }
        return true;
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {

            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {

                return false;
            }

            if (shapeAt(x, y) != Shape.Tetrominoe.NoShape) {

                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();

        return true;
    }

    private void removeVerticalMatches() {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT - 4; y++) {
                Color current = boardColors[(y * BOARD_WIDTH) + x];
                if (current != null && 
                    current.equals(boardColors[((y + 1) * BOARD_WIDTH) + x]) &&
                    current.equals(boardColors[((y + 2) * BOARD_WIDTH) + x]) &&
                    current.equals(boardColors[((y + 3) * BOARD_WIDTH) + x]) &&
                    current.equals(boardColors[((y + 4) * BOARD_WIDTH) + x])) {
    
                    // Remove matched blocks and update score
                    for (int i = y; i < y + 5; i++) {
                        boardColors[(i * BOARD_WIDTH) + x] = null;
                        board[(i * BOARD_WIDTH) + x] = Shape.Tetrominoe.NoShape;
                    }
    
                    numLinesRemoved += 1;
                    repaint(); // Repaint after changes
                    updateScoreDisplay();
                }
            }
        }
    }
    
    private void updateScoreDisplay() {
        statusbar.setText(String.valueOf(numLinesRemoved));
    }

    private void removeFullLines() {

        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {

            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {

                if (shapeAt(j, i) == Shape.Tetrominoe.NoShape) {

                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {

                numFullLines++;

                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {

            numLinesRemoved += numFullLines;

            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            curPiece.setShape(Shape.Tetrominoe.NoShape);
        }
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tetrominoe shape) {

        Color colors[] = {
            new Color(111, 87, 193),
            new Color(58, 100, 82),
            new Color(124, 118, 243),
            new Color(22, 73, 147),
            new Color(77, 198, 16),
            new Color(180, 19, 44),
            new Color(170, 49, 222),
            new Color(161, 29, 155),
            new Color(56, 83, 167),
            new Color(12, 133, 200)
        };

        var color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }   

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private void update() {

        if (isPaused) {

            return;
        }

        if (isFallingFinished) {

            isFallingFinished = false;
            newPiece();
        } else {

            oneLineDown();
        }
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
    
            if (curPiece.getShape() == Shape.Tetrominoe.NoShape) {
                return;
            }
    
            int keycode = e.getKeyCode();

            switch (keycode) {
                case KeyEvent.VK_P:
                    pause();
                    break;
                case KeyEvent.VK_LEFT:
                    tryMove(curPiece, curX - 1, curY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(curPiece, curX + 1, curY);
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(curPiece.rotateRight(), curX, curY);
                    break;
                case KeyEvent.VK_UP:
                    tryMove(curPiece.rotateLeft(), curX, curY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
                case KeyEvent.VK_D:
                    oneLineDown();
                    break;
            }
        }
    }    
}
