package com.comp2042.model.data;

/**
 * Immutable data class representing the result of a move down operation.
 * Combines line clear information (if any) with updated view data for rendering.
 */
public final class MoveDownResult {
    private final LineClearResult clearRow;
    private final ViewData viewData;

    /**
     * Constructs a new move down result.
     *
     * @param clearRow the line clear result, or null if no lines cleared
     * @param viewData the updated view data for rendering
     */
    public MoveDownResult(LineClearResult clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the line clear result from this move.
     *
     * @return LineClearResult if lines were cleared, null otherwise
     */
    public LineClearResult getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data for rendering.
     * Contains current brick position, next brick, shadow position, etc.
     *
     * @return ViewData object for rendering the game state
     */
    public ViewData getViewData() {
        return viewData;
    }
}
