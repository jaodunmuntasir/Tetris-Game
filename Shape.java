/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetrisgame;

/**
 *
 * @author Asus
 */
import java.util.Random;
import java.awt.Color;

public class Shape {

    protected enum Tetrominoe { NoShape, ZShape, SShape, LineShape,
        TShape, SquareShape, LShape, MirroredLShape }

    private Tetrominoe pieceShape;
    private int coords[][];
    private int[][][] coordsTable;
    private Color color;

    private static final Color[] SHAPE_COLORS = {
        new Color(0, 0, 0), // NoShape
        new Color(111, 87, 193), // ZShape
        new Color(58, 100, 82), // SShape
        new Color(124, 118, 243), // LineShape
        new Color(22, 73, 147), // TShape
        new Color(77, 198, 16), // SquareShape
        new Color(180, 19, 44), // LShape
        new Color(170, 49, 222), // MirroredLShape
    };

    public Shape() {
        initShape();
    }

    private void initShape() {
        coords = new int[4][2];
        coordsTable = new int[][][]{
            { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
            { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },
            { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
            { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
            { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },
            { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
            { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
            { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }
        };
        setShape(Tetrominoe.NoShape);
    }

    public Color getColor() {
        System.out.println("Shape created with color: " + color);
        return color;
    }

    public void setShape(Tetrominoe shape) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
        color = SHAPE_COLORS[shape.ordinal()]; // Assign color based on shape
    }

    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Tetrominoe getShape()  { return pieceShape; }

    public void setRandomShape() {

        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        Tetrominoe[] values = Tetrominoe.values();
        setShape(values[x]);
    }

    public int minX() {

        int m = coords[0][0];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, coords[i][0]);
        }

        return m;
    }


    public int minY() {

        int m = coords[0][1];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, coords[i][1]);
        }

        return m;
    }

    public Shape rotateLeft() {

        if (pieceShape == Tetrominoe.SquareShape) {

            return this;
        }

        var result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {

            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    public Shape rotateRight() {

        if (pieceShape == Tetrominoe.SquareShape) {

            return this;
        }

        var result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {

            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }
}
