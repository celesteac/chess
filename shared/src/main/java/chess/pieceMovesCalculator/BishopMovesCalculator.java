package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {
    public BishopMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        for(int j = 1; j < 5; j++) { //4 movement directions
            for (int i = 1; i < 8; i++) {  //so it doesn't go off the board

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

                if(orderOfOps(tempRow,tempCol)) break;

            }
        }

        return this.legalMoves;
    }
}
