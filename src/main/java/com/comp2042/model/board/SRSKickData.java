package com.comp2042.model.board;

import com.comp2042.model.brick.Brick;

/**
 * Provides Super Rotation System (SRS) kick data for Tetris pieces.
 * The SRS defines how pieces "kick" (shift positions) when rotated near walls or other obstacles, ensuring smoother gameplay.
 * This class stores the official kick data for different brick types (J, L, S, T, Z, and I) and
 * provides utility methods to retrieve the appropriate kick offsets based
 * on rotation direction and state transitions.
 */
public class SRSKickData {

    /**
     * Kick data for J, L, S, T, and Z pieces when rotated counter-clockwise.
     * Each entry corresponds to a state transition.
     * The inner arrays represent possible offset adjustments (x, y) applied sequentially until a valid position is found.
     */
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

    /**
     * Kick data for the I-piece when rotated counter-clockwise.
     */
    private static final int[][][] I_CCW = {
            // State 0 -> 3 (horizontal spawn -> vertical left)
            {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // State 1 -> 0 (vertical right -> horizontal spawn) - kicks LEFT for right wall
            {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
            // State 2 -> 1 (horizontal reverse -> vertical right)
            {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}}
    };

    /**
     * Retrieves the appropriate kick offsets for a given brick rotation.
     *
     * @param brick     The Brick being rotated.
     * @param from      The current rotation state index of the brick.
     * @param to        The target rotation state index of the brick.
     * @param clockwise true if the rotation is clockwise, false if counter-clockwise.
     *
     * @return A 2D array of offset pairs (x, y) representing possible kicks.
     *         Each offset is applied in sequence until a valid position is found.
     *         Returns {{0, 0}} if no kicks are applicable (e.g., O-piece).
     */
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