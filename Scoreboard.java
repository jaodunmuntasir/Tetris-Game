/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetrisgame;

/**
 *
 * @author Asus
 */
import java.sql.*;
import java.util.*;

public class Scoreboard {
    private static final String DB_URL = "jdbc:sqlite:tetris.db";

    public Scoreboard() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS scores (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, score INTEGER NOT NULL)";
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
        }
    }

    public void addScore(String name, int score) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO scores (name, score) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, score);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
        }
    }

    public List<ScoreEntry> getTopScores(int topN) {
        List<ScoreEntry> topScores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT name, score FROM scores ORDER BY score DESC LIMIT ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, topN);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    topScores.add(new ScoreEntry(rs.getString("name"), rs.getInt("score")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
        }
        return topScores;
    }

    public static class ScoreEntry {
        private final String name;
        private final int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}

