package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {
    public BishopMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        int row = position.getRow();     //is in array indices
        int col = position.getColumn(); //is in array indices

        for(int j = 1; j < 5; j++) {
            for (int i = 1; i < 8; i++) {

                int tempRow = switch(j){
                    case 1 -> row + i;
                    case 2 -> row + i;
                    case 3 -> row - i;
                    case 4 -> row - i;
                    default -> row;
                };

                int tempCol = switch (j){
                    case 1 -> col + i;
                    case 2 -> col - i;
                    case 3 -> col + i;
                    case 4 -> col - i;
                    default -> col;
                };

                tempRow = position.convertRowToChessIndices(tempRow);
                tempCol = position.convertColToChessIndices(tempCol);
                ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

                if (checkBounds(tempRow, tempCol)) {
                    if (checkEmpty(tempPosition)) {
                        addMove(tempPosition);
                    } else if (checkEnemy(tempPosition)) {
                        addMove(tempPosition);
                        break;
                    } else break;
                } else break;

            }
        }




        return this.legalMoves;
    }
}
