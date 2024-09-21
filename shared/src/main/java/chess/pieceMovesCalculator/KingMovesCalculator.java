package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
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

                    if(checkBounds(tempRow,tempCol)){
                        if(checkEmpty(tempPosition)) {
                            addMove(tempPosition);
                        }
                        else if(checkEnemy(tempPosition)){
                            addMove(tempPosition);
                        }
                    }

                }
            }
        }

        return this.legalMoves;
    }

}
