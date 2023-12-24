/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetrisgame;

/**
 *
 * @author Asus
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.Box;

public class Tetris extends JFrame {

    private JLabel statusbar;
    private Board board;
    private Scoreboard scoreboard;
    private String playerName;

    public Tetris() {
        if (showStartGameDialog()) {
            playerName = getPlayerName(); // Store the player's name
            scoreboard = new Scoreboard(); // Initialize the scoreboard here
            initUI();
        } else {
            System.exit(0); // Exit the application if the user clicks "Exit"
        }
    }


    private boolean showStartGameDialog() {
        final boolean[] startGame = {false}; // Array to hold the user's choice
    
        JPanel panel = new JPanel();
        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");
    
        panel.add(startButton);
        panel.add(exitButton);
    
        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = pane.createDialog("Tetris");
    
        startButton.addActionListener(e -> {
            startGame[0] = true;
            dialog.dispose(); // Close dialog on click
        });
        exitButton.addActionListener(e -> {
            startGame[0] = false;
            dialog.dispose(); // Close dialog on click
        });
    
        dialog.setVisible(true);
    
        return startGame[0]; // Return true if Start Game was clicked
    } 

    private void initUI() {

        statusbar = new JLabel(" 0");
        statusbar.setFont(new Font("Serif", Font.BOLD, 40));
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        statusbar.setVerticalAlignment(SwingConstants.CENTER);
        add(statusbar, BorderLayout.SOUTH);

        JPanel scorePanel = new JPanel(new BorderLayout());
        statusbar.setFont(new Font("Serif", Font.BOLD, 40));
        scorePanel.add(statusbar, BorderLayout.NORTH);

        // Create buttons
        Dimension buttonSize = new Dimension(150, 50);
        JButton quitButton = new JButton("Quit Game");
        JButton restartButton = new JButton("Restart Game");
        JButton pauseButton = new JButton("Pause Game");
        
        // Set the same size and margin for all buttons
        quitButton.setPreferredSize(buttonSize);
        quitButton.setMargin(new Insets(10, 10, 10, 10)); // Adjust top, left, bottom, right margins

        restartButton.setPreferredSize(buttonSize);
        restartButton.setMargin(new Insets(10, 10, 10, 10));

        pauseButton.setPreferredSize(buttonSize);
        pauseButton.setMargin(new Insets(10, 10, 10, 10));

        // Add action listeners to buttons
        quitButton.addActionListener(e -> confirmAndQuit());
        restartButton.addActionListener(e -> confirmAndRestart());
        pauseButton.addActionListener(e -> board.pause());

        // Button panel for vertical layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalStrut(10)); // Spacer at the top
        buttonPanel.add(quitButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Spacer between buttons
        buttonPanel.add(restartButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(pauseButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Spacer at the bottom


        scorePanel.add(buttonPanel, BorderLayout.SOUTH);

        board = new Board(this);
        board.setPlayerName(playerName); 
        add(board);
        board.start();
        add(scorePanel, BorderLayout.EAST);

        setTitle("Tetris");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    JLabel getStatusBar() {

        return statusbar;
    }
    
    private String getPlayerName() {
        return JOptionPane.showInputDialog(this, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
    }
    
    public void gameOver(int score) {
        // Save the score using the stored player's name
        scoreboard.addScore(playerName, score);

        // Close the game window and show top scores
        this.dispose();
        showTopScores();
    }
    
    private void showTopScores() {
        List<Scoreboard.ScoreEntry> topScores = scoreboard.getTopScores(10);

        // Create column names
        String[] columnNames = {"Name", "Score"};

        // Create table data
        Object[][] data = new Object[topScores.size()][2];
        for (int i = 0; i < topScores.size(); i++) {
            Scoreboard.ScoreEntry entry = topScores.get(i);
            data[i][0] = entry.getName();
            data[i][1] = entry.getScore();
        }

        // Create table model and set it to the table
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);

        // Create buttons
        JButton restartButton = new JButton("Restart");
        JButton exitButton = new JButton("Exit");

        // Add action listeners to buttons
        restartButton.addActionListener(e -> restartGame());
        exitButton.addActionListener(e -> System.exit(0));

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);

        // Set up the frame to display the table and buttons
        JFrame scoresFrame = new JFrame("Top Scores");
        scoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        scoresFrame.setLayout(new BorderLayout());
        scoresFrame.add(new JScrollPane(table), BorderLayout.CENTER);
        scoresFrame.add(buttonPanel, BorderLayout.SOUTH);
        scoresFrame.pack();
        scoresFrame.setLocationRelativeTo(null);
        scoresFrame.setVisible(true);
    }
    
    private void confirmAndQuit() {
        board.pause();
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to Quit the Game without saving?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void confirmAndRestart() {
        board.pause();
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to Restart the Game without saving?", "Confirm Restart", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        }
    }
    
    private void restartGame() {
        this.dispose(); // Close the current window
        EventQueue.invokeLater(() -> {
            var game = new Tetris();
            game.setVisible(true);
        });
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var game = new Tetris();
            game.setVisible(true);
        });
    }
}
