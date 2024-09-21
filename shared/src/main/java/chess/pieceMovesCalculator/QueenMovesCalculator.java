package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        for(int j = 1; j < 9; j++) { //8 movement directions
            for (int i = 1; i < 8; i++) { //so it doesn't go off the board

                int tempRow = switch(j){ //moving around the original position, top left to counterclockwise
                    case 1 -> row + i;
                    case 2 -> row + i;
                    case 3 -> row + i;
                    case 4 -> row;
                    case 5 -> row - i;
                    case 6 -> row - i;
                    case 7 -> row - i;
                    case 8 -> row;
                    default -> row;
                };

                int tempCol = switch (j){ //moving around the original position, top left to counterclockwise
                    case 1 -> col - i;
                    case 2 -> col;
                    case 3 -> col + i;
                    case 4 -> col + i;
                    case 5 -> col + i;
                    case 6 -> col;
                    case 7 -> col - i;
                    case 8 -> col - i;
                    default -> col;
                };

                if(orderOfOps(tempRow,tempCol)) break;

            }
        }

        return this.legalMoves;
    }
}
