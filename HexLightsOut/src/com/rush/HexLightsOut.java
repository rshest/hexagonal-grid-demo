package com.rush;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Example applet which uses hexagonal grid. It's a hexagonal version of the
 * "lights out" puzzle game: http://en.wikipedia.org/wiki/Lights_Out_(game)
 * 
 * @author Ruslan Shestopalyuk
 */
public class HexLightsOut extends Applet implements MouseListener {
    private static final long serialVersionUID = 1L;

    private static final int BOARD_WIDTH = 5;
    private static final int BOARD_HEIGHT = 4;

    private static final int L_ON = 1;
    private static final int L_OFF = 2;

    private static final int NUM_HEX_CORNERS = 6;
    private static final int CELL_RADIUS = 40;

    //  game board cells array
    private int[][] mCells = { {    0, L_ON, L_ON, L_ON,    0 }, 
                               { L_ON, L_ON, L_ON, L_ON, L_ON }, 
                               { L_ON, L_ON, L_ON, L_ON, L_ON },
                               {    0,    0, L_ON,    0,    0 } };

    private int[] mCornersX = new int[NUM_HEX_CORNERS];
    private int[] mCornersY = new int[NUM_HEX_CORNERS];

    private static HexGridCell mCellMetrics = new HexGridCell(CELL_RADIUS);

    @Override
    public void init() {
        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                mCellMetrics.setCellIndex(i, j);
                if (mCells[j][i] != 0) {
                    mCellMetrics.computeCorners(mCornersX, mCornersY);

                    g.setColor((mCells[j][i] == L_ON) ? Color.ORANGE : Color.GRAY);
                    g.fillPolygon(mCornersX, mCornersY, NUM_HEX_CORNERS);
                    g.setColor(Color.BLACK);
                    g.drawPolygon(mCornersX, mCornersY, NUM_HEX_CORNERS);
                }
            }
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Returns true if cell is inside the game board.
     * 
     * @param i cell's horizontal index
     * @param j cell's vertical index
     */
    private boolean isInsideBoard(int i, int j) {
        return i >= 0 && i < BOARD_WIDTH && j >= 0 && j < BOARD_HEIGHT
                && mCells[j][i] != 0;
    }

    /**
     * Toggles the cell's light ON<->OFF.
     */
    private void toggleCell(int i, int j) {
        mCells[j][i] = (mCells[j][i] == L_ON) ? L_OFF : L_ON;
    }

    /**
     * Returns true if all lights have been switched off.
     */
    private boolean isWinCondition() {
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                if (mCells[j][i] == L_ON) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Resets the game to the initial position (all lights are on).
     */
    private void resetGame() {
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                if (mCells[j][i] == L_OFF) {
                    mCells[j][i] = L_ON;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        mCellMetrics.setCellByPoint(arg0.getX(), arg0.getY());
        int clickI = mCellMetrics.getIndexI();
        int clickJ = mCellMetrics.getIndexJ();

        if (isInsideBoard(clickI, clickJ)) {
            // toggle the clicked cell together with the neighbors
            toggleCell(clickI, clickJ);
            for (int k = 0; k < 6; k++) {
                int nI = mCellMetrics.getNeighborI(k);
                int nJ = mCellMetrics.getNeighborJ(k);
                if (isInsideBoard(nI, nJ)) {
                    toggleCell(nI, nJ);
                }
            }
        }
        repaint();

        if (isWinCondition()) {
            JOptionPane.showMessageDialog(new JFrame(), "Well done!");
            resetGame();
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }
}
