package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition implements Cloneable {
    private final int col;
    private final int row;

    public ChessPosition(int row, int col) {
        this.col = convertColToArrayIndices(col);
        this.row = convertRowIndices(row);

    }


    public int convertColToChessIndices(int inCol){
        return inCol +1;
    }

    public int convertRowIndices(int inRow) {
        return switch(inRow){
            case -1 -> 9;
            case 0 -> 8;
            case 1 -> 7;
            case 2 -> 6;
            case 3 -> 5;
            case 4 -> 4;
            case 5 -> 3;
            case 6 -> 2;
            case 7 -> 1;
            case 8 -> 0;
            case 9 -> -1;
            default -> -10; //error
        };
    }

    private int convertColToArrayIndices(int inCol){
        return inCol -1;
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

        int colPrint = convertColToChessIndices(this.col);
        int rowPrint = convertRowIndices(this.row);

        return rowPrint + "," + colPrint;
//        return "[" + rowPrint + "," + colPrint + "]";
    }

    @Override
    public ChessPosition clone(){
        try {
            return (ChessPosition) super.clone();
        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        };
        if (o == null || getClass() != o.getClass()) {
            return false;
        };
        ChessPosition that = (ChessPosition) o;
        return (col == that.col && row == that.row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
