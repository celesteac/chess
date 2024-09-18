package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int col;
    private final int row;

    public ChessPosition(int row, int col) {
//        this.row = convertRow(row, true);
//        this.col = convertColumn(col, true);
        this.col = col-1;
        this.row = switch (row){
            case 1 -> 7;
            case 2 -> 6;
            case 3 -> 5;
            case 4 -> 4;
            case 5 -> 3;
            case 6 -> 2;
            case 7 -> 1;
            case 8 -> 0;
            default -> 1;
        };

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public String toString() {
        return "[" + row + "," + col + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return (col == that.col && row == that.row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
