package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMovesCalculator extends PieceMovesCalculator{

    public KnightMovesCalculator(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public ArrayList<ChessMove> calculateMoves(){

        for(int j = 0; j < 8; j++){ // 8 movement directions

            int rowMod = switch (j){
                case 0 -> 1;  //up right top
                case 1 -> 2; //up right side
                case 2 -> -2; //down right side
                case 3 -> -1;  //down right bottom
                case 4 -> -1;  //down left bottom
                case 5 -> -2;  //down left side
                case 6 -> 2;  //up left side
                case 7 -> 1;  //up left top
                default -> 0;
            };

            int colMod = switch (j){
                case 0 -> 2;  //up right top
                case 1 -> 1;  //up right side
                case 2 -> 1;  //down right side
                case 3 -> 2;  //down right bottom
                case 4 -> -2;  //down left bottom
                case 5 -> -1;  //down left side
                case 6 -> -1;  //up left side
                case 7 -> -2;  //up left top
                default -> 0;
            };

            orderOfOperations(rowMod, colMod);

        }


        return legalMoves;
    }
}
