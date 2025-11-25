package comp2042.model.data;

/**
 * Result of a move down operation
 */
public final class MoveDownResult {
    private final LineClearResult clearRow;
    private final ViewData viewData;

    public MoveDownResult(LineClearResult clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public LineClearResult getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
