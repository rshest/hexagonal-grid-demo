package com.rush;

/**
 * Uniform hexagonal grid cell's metrics utility class.
 * 
 * @author Ruslan Shestopalyuk
 */
public class HexGridCell {
    private static final int[] NEIGHBORS_DI = { 0, 1, 1, 0, -1, -1 };
    private static final int[][] NEIGHBORS_DJ = { 
            { -1, -1, 0, 1, 0, -1 }, { -1, 0, 1, 1, 1, 0 } };

    private final int[] CORNERS_DX; // array of horizontal offsets of the cell's corners
    private final int[] CORNERS_DY; // array of vertical offsets of the cell's corners
    private final int SIDE;

    private int mX = 0; // cell's left coordinate
    private int mY = 0; // cell's top coordinate

    private int mI = 0; // cell's horizontal grid coordinate
    private int mJ = 0; // cell's vertical grid coordinate

    /**
     * Cell radius (distance from center to one of the corners)
     */
    public final int RADIUS;
    /**
     * Cell height
     */
    public final int HEIGHT;
    /**
     * Cell width
     */
    public final int WIDTH;

    public static final int NUM_NEIGHBORS = 6;

    /**
     * @param radius Cell radius (distance from the center to one of the corners)
     */
    public HexGridCell(int radius) {
        RADIUS = radius;
        WIDTH = radius * 2;
        HEIGHT = (int) (((float) radius) * Math.sqrt(3));
        SIDE = radius * 3 / 2;

        int cdx[] = { RADIUS / 2, SIDE, WIDTH, SIDE, RADIUS / 2, 0 };
        CORNERS_DX = cdx;
        int cdy[] = { 0, 0, HEIGHT / 2, HEIGHT, HEIGHT, HEIGHT / 2 };
        CORNERS_DY = cdy;
    }

    /**
     * @return X coordinate of the cell's top left corner.
     */
    public int getLeft() {
        return mX;
    }

    /**
     * @return Y coordinate of the cell's top left corner.
     */
    public int getTop() {
        return mY;
    }

    /**
     * @return X coordinate of the cell's center
     */
    public int getCenterX() {
        return mX + RADIUS;
    }

    /**
     * @return Y coordinate of the cell's center
     */
    public int getCenterY() {
        return mY + HEIGHT / 2;
    }

    /**
     * @return Horizontal grid coordinate for the cell.
     */
    public int getIndexI() {
        return mI;
    }

    /**
     * @return Vertical grid coordinate for the cell.
     */
    public int getIndexJ() {
        return mJ;
    }

    /**
     * @return Horizontal grid coordinate for the given neighbor.
     */
    public int getNeighborI(int neighborIdx) {
        return mI + NEIGHBORS_DI[neighborIdx];
    }

    /**
     * @return Vertical grid coordinate for the given neighbor.
     */
    public int getNeighborJ(int neighborIdx) {
        return mJ + NEIGHBORS_DJ[mI % 2][neighborIdx];
    }

    /**
     * Computes X and Y coordinates for all of the cell's 6 corners, clockwise,
     * starting from the top left.
     * 
     * @param cornersX Array to fill in with X coordinates of the cell's corners
     * @param cornersX Array to fill in with Y coordinates of the cell's corners
     */
    public void computeCorners(int[] cornersX, int[] cornersY) {
        for (int k = 0; k < NUM_NEIGHBORS; k++) {
            cornersX[k] = mX + CORNERS_DX[k];
            cornersY[k] = mY + CORNERS_DY[k];
        }
    }

    /**
     * Sets the cell's horizontal and vertical grid coordinates.
     */
    public void setCellIndex(int i, int j) {
        mI = i;
        mJ = j;
        mX = i * SIDE;
        mY = HEIGHT * (2 * j + (i % 2)) / 2;
    }
    
    /**
     * Sets the cell as corresponding to some point inside it (can be used for
     * e.g. mouse picking).
     */
    public void setCellByPoint(int x, int y) {
        int ci = (int)Math.floor((float)x/(float)SIDE);
        int cx = x - SIDE*ci;

        int ty = y - (ci % 2) * HEIGHT / 2;
        int cj = (int)Math.floor((float)ty/(float)HEIGHT);
        int cy = ty - HEIGHT*cj;

        if (cx > Math.abs(RADIUS / 2 - RADIUS * cy / HEIGHT)) {
            setCellIndex(ci, cj);
        } else {
            setCellIndex(ci - 1, cj + (ci % 2) - ((cy < HEIGHT / 2) ? 1 : 0));
        }
    }
}
