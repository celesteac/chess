package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){

        int[] indices = {-1,0,1};  //loop through all the spaces around the king
        for(int i : indices){
            for(int j : indices){
                if(!(i == 0 && j == 0)){  //don't check the original position

                    orderOfOps(row+i, col+j);

                }
            }
        }

        return this.legalMoves;
    }

}
