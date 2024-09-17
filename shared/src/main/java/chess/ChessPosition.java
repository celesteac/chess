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

        this.row = convertRow(row, true);
        this.col = convertColumn(col, true);
    }


    //the convert functions take a row and column that use the chessboard indices
    //and convert them to array indices

    public int convertColumn(int inCol, boolean toArray){
        int outCol;
        if(toArray) outCol = inCol - 1;
        else outCol = inCol + 1;
        return outCol;
    }

    public int convertRow(int inRow, boolean toArray){
        int outRow;

        if(!toArray) outRow = inRow + 1;
        outRow = switch (inRow) {
            case 1 -> 8;
            case 2 -> 7;
            case 3 -> 6;
            case 4 -> 5;
            case 5 -> 4;
            case 6 -> 3;
            case 7 -> 2;
            case 8 -> 1;
            default -> 1;
        };

        if(toArray) outRow = outRow - 1;

        return outRow;
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
        int col = convertColumn(this.col, false);
        int row = convertRow(this.row, false);

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
