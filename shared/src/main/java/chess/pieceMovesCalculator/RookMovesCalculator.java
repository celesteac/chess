package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {
    public RookMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        for(int j = 1; j < 5; j++) {
            for (int i = 1; i < 8; i++) {

                int tempRow = switch(j){
                    case 1 -> row + i;
                    case 2 -> row - i;
                    default -> row;
                };

                int tempCol = switch (j){
                    case 3 -> col + i;
                    case 4 -> col - i;
                    default -> col;
                };

                if(orderOfOps(tempRow,tempCol)) break;

            }
        }

        return this.legalMoves;
    }

}


