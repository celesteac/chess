package chess.piecemovescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMovesCalculator extends PieceMovesCalculator{

    public RookMovesCalculator(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public ArrayList<ChessMove> calculateMoves(){

        for(int j = 0; j < 4; j++){ // 4 movement directions
            for(int i = 1; i < 8; i ++){ //not go off the board

                int rowMod = switch (j){
                    case 0 -> i; //up
                    case 1 -> 0; //right
                    case 2 -> -i; //down
                    case 3 -> 0; //left
                    default -> 0;
                };

                int colMod = switch (j){
                    case 0 -> 0; //up
                    case 1 -> i; //right
                    case 2 -> 0; //down
                    case 3 -> -i; //left
                    default -> 0;
                };

                if(orderOfOperations(rowMod, colMod)) break;

            }
        }


        return legalMoves;
    }
}
