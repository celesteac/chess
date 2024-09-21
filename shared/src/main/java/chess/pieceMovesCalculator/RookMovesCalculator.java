package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {
    public RookMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        int row = position.getRow();     //is in array indices
        int col = position.getColumn(); //is in array indices

        for(int i = 1; i < 8; i++){

            int tempRow = position.convertRowToChessIndices(row+i);
            int tempCol = position.convertColToChessIndices(col);
            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

            if(checkBounds(tempRow,tempCol)){
                if(checkEmpty(tempPosition)){
                    addMove(tempPosition);
                }
                else if(checkEnemy(tempPosition)){
                    addMove(tempPosition);
                }
            }
            else break;

        }

        return this.legalMoves;
    }

}


