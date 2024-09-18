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

    public ArrayList<ChessMove> calculateLegalMoves(){
        //if a piece is by the edge or a piece, it cannot move there
        //a king can move into any of the spaces around it

        //write a loop that checks all the spaces around the king
            //if the space is empty, you can move into it

        int row = position.getRow();     //is in array indices
        int col = position.getColumn(); //is in array indices

        int[] indices = {-1,0,1};
        for(int i : indices){
            for(int j : indices){
//                System.out.println("i = " + i + ", j = " + j);
                if(!(i == 0 && j == 0)){

                    int tempRow = position.convertRowToChessIndices(row+i);
                    int tempCol = position.convertColToChessIndices(col+j);
                    ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);
                    System.out.println(new ChessPosition(tempRow, tempCol));

                    if(tempRow < 9 && tempRow > 0 && tempCol < 9 && tempCol > 0){
                        if(board.getPiece(tempPosition) == null) {
                            legalMoves.add(new ChessMove(position, tempPosition, null));
                        }
                    }

                }
            }
        }

//        legalMoves.add(new ChessMove(position, position, null));

        return this.legalMoves;
    }

    //accounts for pieces blocking path
    //does not ask if the king is being attacked or whose turn it is
    //does it care if you can take a piece? I think it doesn't take pieces?
}
