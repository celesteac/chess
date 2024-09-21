package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    public PawnMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    private int getRowIfWhite(int j){
        return switch (j) {
            case 0 -> row -1;  //diagonals
            case 1 -> row -1;  //diagonals
            case 2 -> row -1;  //forward 1
            case 3 -> row -2;  //forward 2
            default -> row;
        };
    }

    private int getRowIfBlack(int j){
        return switch (j) {
            case 0 -> row +1;  //diagonals
            case 1 -> row +1;  //diagonals
            case 2 -> row +1;  //forward 1
            case 3 -> row +2;  //forward 2
            default -> row;
        };
    }

    private int getColumn(int j){
        return switch (j){
            case 0 -> col -1;  //diagonals
            case 1 -> col +1;  //diagonals
            case 2 -> col;     //forward 1
            case 3 -> col;     //forward 2
            default -> col;
        };
    }

    public Collection<ChessMove> calculateLegalMoves(){

        int possibleMoves =
                (color == ChessGame.TeamColor.BLACK && row == 1)    //if is black and first move
                || (color == ChessGame.TeamColor.WHITE && row == 6) //or if is white and first move
                ? 4 : 3; //possible moves is 4, otherwise 3

        for(int j = 0; j < possibleMoves; j++) {

            int tempRow = (color == ChessGame.TeamColor.BLACK) ? getRowIfBlack(j) : getRowIfWhite(j);
            int tempCol = getColumn(j);

            tempRow = position.convertRowToChessIndices(tempRow);
            tempCol = position.convertColToChessIndices(tempCol);
            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);


            if (checkBounds(tempRow, tempCol)) {
                if (j > 1) { //cases forward 1 or 2 spaces
                    if (checkEmpty(tempPosition)) {
                        addMove(tempPosition);
                    }
                    else if(j == 2) break; //piece blocking move 2 action
                }
                else { //diagonals
                    if (!checkEmpty(tempPosition) && checkEnemy(tempPosition)) {
                        addMove(tempPosition);
                    }
                }
            }
        }


        return this.legalMoves;
    }
}
