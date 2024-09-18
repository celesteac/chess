package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    private int convertRowToChessIndices(int inRow) {
        return switch(inRow){
            case 0 -> 8;
            case 1 -> 7;
            case 2 -> 6;
            case 3 -> 5;
            case 4 -> 4;
            case 5 -> 3;
            case 6 -> 2;
            case 7 -> 1;
            default -> -1;
        };
    }

    private int convertColToChessIndices(int col){
        return col +1;
    }

    public ArrayList<ChessMove> calculateLegalMoves(){
        //if a piece is by the edge or a piece, it cannot move there
        //a king can move into any of the spaces around it

        //write a loop that checks all the spaces around the king
            //if the space is empty, you can move into it

        int row = position.getRow();     //is in array indices
        int col = position.getColumn(); //is in array indices

//        row = convertRowToChessIndices(row);  //to convert to chess indices
//        col = convertColToChessIndices(col); //so that we can make a new position variable

//        System.out.println(position);
//        System.out.println(new ChessPosition(row, col));

        int[] indices = {-1,0,1};
        for(int i : indices){
            for(int j : indices){
                int tempRow = convertRowToChessIndices(row+i);
                int tempCol = convertColToChessIndices(col+j);
                System.out.println(new ChessPosition(tempRow, tempCol));
            }
        }

        legalMoves.add(new ChessMove(position, position, null));

        return this.legalMoves;
    }

    //accounts for pieces blocking path
    //does not ask if the king is being attacked or whose turn it is
    //does it care if you can take a piece? I think it doesn't take pieces?
}
