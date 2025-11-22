package com.comp2042;

import com.comp2042.logic.bricks.Brick;

public class SRSKickData {

    private static final int[][][] JLSTZ_CCW = {
            // State 0 -> 3 (spawn -> left)
            {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
            // State 1 -> 0 (right -> spawn) - needs both directions
            {{0, 0}, {1, 0}, {-1, 0}, {1, 1}, {-1, 1}},
            // State 2 -> 1 (reverse -> right)
            {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
            // State 3 -> 2 (left -> reverse) - needs both directions
            {{0, 0}, {1, 0}, {-1, 0}, {1, 1}, {-1, 1}}
    };

    // Official SRS kick data for I-piece (counter-clockwise rotation)
    private static final int[][][] I_CCW = {
            // State 0 -> 3 (horizontal spawn -> vertical left)
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // State 1 -> 0 (vertical right -> horizontal spawn) - kicks LEFT for right wall
            {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
            // State 2 -> 1 (horizontal reverse -> vertical right)
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}
    };

    public static int[][] getKicks(Brick brick, int from, int to, boolean clockwise) {
        String brickType = brick.getClass().getSimpleName();

        if (brickType.equals("OBrick")) {
            return new int[][]{{0, 0}};
        }
        if (!clockwise) {
            if (brickType.equals("IBrick")) {
                int numStates = brick.getShapeMatrix().size();
                if (numStates == 2) {
                    if (from == 0) return I_CCW[0];
                    if (from == 1) return I_CCW[1];
                } else {
                    return I_CCW[from];
                }
            } else {
                return JLSTZ_CCW[from];
            }
        }

        // Fallback
        return new int[][]{{0, 0}};
    }
}