package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMovesCalculator extends PieceMovesCalculator{

    public BishopMovesCalculator(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public ArrayList<ChessMove> calculateMoves(){

        for(int j = 0; j < 4; j++){ // 4 movement directions
            for(int i = 1; i < 8; i ++){ //not go off the board

                int rowMod = switch (j){
                    case 0 -> i; //up right
                    case 1 -> -i; //down right
                    case 2 -> -i; //down left
                    case 3 -> i; //up left
                    default -> 0;
                };

                int colMod = switch (j){
                    case 0 -> i; //up right
                    case 1 -> i; //down right
                    case 2 -> -i; //down left
                    case 3 -> -i; //up left
                    default -> 0;
                };

                if(orderOfOperations(rowMod, colMod)) break;

            }
        }

        return legalMoves;
    }
}
