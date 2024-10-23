package chess.piecemovescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class QueenMovesCalculator extends PieceMovesCalculator{

    public QueenMovesCalculator(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public ArrayList<ChessMove> calculateMoves(){

        for(int j = 0; j < 8; j++){ // 8 movement directions
            for(int i = 1; i < 8; i ++){ //not go off the board

                int rowMod = switch (j){
                    case 0 -> i;  //up right
                    case 1 -> 0; //right
                    case 2 -> -i; //down right
                    case 3 -> -i;  //down
                    case 4 -> -i;  //down left
                    case 5 -> 0;  //left
                    case 6 -> i;  //up left
                    case 7 -> i;  //up
                    default -> 0;
                };

                int colMod = switch (j){
                    case 0 -> i;  //up right
                    case 1 -> i; //right
                    case 2 -> i; //down right
                    case 3 -> 0;  //down
                    case 4 -> -i;  //down left
                    case 5 -> -i;  //left
                    case 6 -> -i;  //up left
                    case 7 -> 0;  //up
                    default -> 0;
                };

                if(orderOfOperations(rowMod, colMod)) {
                    break;
                };

            }
        }

        return legalMoves;
    }
}
