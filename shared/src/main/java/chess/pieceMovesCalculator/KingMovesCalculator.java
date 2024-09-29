package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KingMovesCalculator extends chess.pieceMovesCalculator.PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public ArrayList<ChessMove> calculateMoves(){

        int[] indices = {-1,0,1};

        for(int i : indices){  //checking every space around the king
            for(int j : indices){
                if(!(i == 0 && j == 0)){

                    orderOfOperations(i,j);

                }
            }
        }

        return legalMoves;
    }
}
