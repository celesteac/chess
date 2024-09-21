package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMovesCalculator {
    ChessBoard board;
    ChessPosition position;
    int row;
    int col;
    ChessGame.TeamColor color;
    Collection<ChessMove> legalMoves = new ArrayList<>();

    public PieceMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        this.position = position;
        this.board = board;
        this.color = color;
        this.row = position.getRow();    //are in array indices, not chess indices
        this.col = position.getColumn(); //see order of ops function for conversion to chess indices
    }

    public abstract Collection<ChessMove> calculateLegalMoves();

    public boolean checkBounds(int row, int col) {
        return (row > 0 && row < 9 && col > 0 && col < 9);
    }
    public boolean checkEmpty(ChessPosition testPosition) {
        return board.getPiece(testPosition) == null;
    }
    public boolean checkEnemy(ChessPosition testPosition) {
        return !board.getPiece(testPosition).getTeamColor().equals(color);
    }

    public void addMove(ChessPosition endPosition){
        legalMoves.add(new ChessMove(position, endPosition, null));
    }


    //the boolean in this function will trigger a break in the for loops
    //of the Rook, Bishop, and Queen
    //while the Knight and King ignore the boolean
    public boolean orderOfOps(int inRow, int inCol) {

        int tempRow = position.convertRowToChessIndices(inRow);
        int tempCol = position.convertColToChessIndices(inCol);
        ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

        if (checkBounds(tempRow, tempCol)) {
            if (checkEmpty(tempPosition)) {
                addMove(tempPosition);
            }
            else if (checkEnemy(tempPosition)) {
                addMove(tempPosition);
                return true;
            }
            else{
                return true;
            }
        }
        else {
            return true;
        }
        return false;
    }


}
