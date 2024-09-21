package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        for(int j = 1; j < 9; j++) { //8 movement directions

            int tempRow = switch(j){ //moving around the original position, top left to counterclockwise
                case 1 -> row + 2;
                case 2 -> row + 2;
                case 3 -> row + 1;
                case 4 -> row - 1;
                case 5 -> row - 2;
                case 6 -> row - 2;
                case 7 -> row - 1;
                case 8 -> row + 1;
                default -> row;
            };

            int tempCol = switch (j){ //moving around the original position, top left to counterclockwise
                case 1 -> col - 1;
                case 2 -> col + 1;
                case 3 -> col + 2;
                case 4 -> col + 2;
                case 5 -> col + 1;
                case 6 -> col - 1;
                case 7 -> col - 2;
                case 8 -> col - 2;
                default -> col;
            };

            orderOfOps(tempRow,tempCol);

        }


        return this.legalMoves;
    }
}
