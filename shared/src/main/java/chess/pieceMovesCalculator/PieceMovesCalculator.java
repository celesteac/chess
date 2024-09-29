package chess.pieceMovesCalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMovesCalculator {
    public final ChessBoard board;
    public final ChessPosition position;
    public final ChessPiece.PieceType type;
    public final ChessGame.TeamColor color;
    public final int row;
    public final int col;
    public ArrayList<ChessMove> legalMoves;

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition){
        this.board = board;
        this.position = myPosition;
        this.type = board.getPiece(position).getPieceType();
        this.color = board.getPiece(position).getTeamColor();
        this.row = position.getRow(); //in array indices
        this.col = position.getColumn(); //in array indices
        this.legalMoves = new ArrayList<>();
    }

    public abstract ArrayList<ChessMove> calculateMoves();

    public boolean checkBounds(int tempRow, int tempCol){
        return (tempRow < 9 && tempCol < 9 && tempRow > 0 && tempCol > 0);
    }

    public boolean checkEmpty(ChessPosition tempPosition){
        return board.getPiece(tempPosition) == null;
    }

    public boolean checkEnemy(ChessPosition tempPosition){
        return !(board.getPiece(tempPosition).getTeamColor() == color);

    }

    public void addMove(ChessPosition tempPosition){
        legalMoves.add(new ChessMove(position, tempPosition, null));
    }

    //the boolean will tell the rook, queen, and bishop to exit their inner for loop
    //the knight and king will ignore the boolean
    public boolean orderOfOperations(int rowMod, int colMod){

        int tempRow = position.convertRowIndices(row + rowMod);
        int tempCol = position.convertColToChessIndices(col + colMod);
        ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

        System.out.println(tempPosition);

        if(checkBounds(tempRow,tempCol)){
            if(checkEmpty(tempPosition)){
                addMove(tempPosition);
            }
            else if(checkEnemy(tempPosition)){
                addMove(tempPosition);
                return true;
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
        return false;
    }
}
