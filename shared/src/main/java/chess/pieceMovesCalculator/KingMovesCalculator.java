package chess.pieceMovesCalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        int row = position.getRow();     //is in array indices
        int col = position.getColumn(); //is in array indices

        int[] indices = {-1,0,1};  //loop through all the spaces around the king
        for(int i : indices){
            for(int j : indices){
                if(!(i == 0 && j == 0)){  //don't check the original position

                    int tempRow = position.convertRowToChessIndices(row+i);
                    int tempCol = position.convertColToChessIndices(col+j);
                    ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);
                    ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
                    System.out.println(new ChessPosition(tempRow, tempCol));


                    if(tempRow < 9 && tempRow > 0 && tempCol < 9 && tempCol > 0){ //within bounds?
//                        System.out.println("within bounds");
                        if(board.getPiece(tempPosition) == null) { //square empty?
//                            System.out.println("empty");
                            legalMoves.add(new ChessMove(position, tempPosition, null));
                        }
                        else{ //square occupied by enemy?
                            ChessGame.TeamColor tempColor = board.getPiece(tempPosition).getTeamColor();
                            if(tempColor != pieceColor){
//                                System.out.println(board.getPiece(tempPosition));
                                legalMoves.add(new ChessMove(position, tempPosition, null));
                            }
                        }
                    }

                }
            }
        }

        return this.legalMoves;
    }

}
