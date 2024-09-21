package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    public PawnMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    private int rowIfBlack(int j){
        return switch (j){
            case 0 -> row + 1;  //diagonals
            case 1 -> row + 1;  //diagonals
            case 2 -> row + 1;  //1 ahead
            case 3 -> row + 2;  //2 ahead
            default -> row;
        };
    }

    private int rowIfWhite(int j){
        return switch (j){
            case 0 -> row - 1;  //diagonals
            case 1 -> row - 1;  //diagonals
            case 2 -> row - 1;  //1 ahead
            case 3 -> row - 2;  //2 ahead
            default -> row;
        };
    }


    public Collection<ChessMove> calculateLegalMoves(){

        int possibilities = 3;
        if(position.getRow() == 2 || position.getRow() == 7){
            possibilities++;
        }

        for(int j = 0; j < possibilities; j++){ // checks 3 or 4 possible moves

            int tempRow = (color == ChessGame.TeamColor.WHITE) ? rowIfWhite(j) : rowIfBlack(j);

            int tempCol = switch (j){
                case 0 -> col - 1; //diagonals
                case 1 -> col + 1; //diagonals
                case 2 -> col;     //1 ahead
                case 3 -> col;     //2 ahead
                default -> col;
            };

            tempRow = position.convertRowToChessIndices(tempRow);
            tempCol = position.convertColToChessIndices(tempCol);
            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

            if(j > 1){ // forward motions
                if(checkBounds(tempRow, tempCol)){
                    if(checkEmpty(tempPosition)){
                        addMove(tempPosition);
                    }
                }
            }

            if(j < 2){ //diagonals
                if(checkBounds(tempRow, tempCol)) {
                    if (!checkEmpty(tempPosition) && checkEnemy(tempPosition)) {
                        addMove(tempPosition);
                    }
                }
            }

        }


        return this.legalMoves;
    }
}
