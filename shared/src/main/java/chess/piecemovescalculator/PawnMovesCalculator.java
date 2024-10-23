package chess.piecemovescalculator;

import chess.*;

import java.util.ArrayList;

public class PawnMovesCalculator extends PieceMovesCalculator{

    public PawnMovesCalculator(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    private int getColMod(int j){
        return switch (j){
            case 0 -> 1;  //diagonal right
            case 1 -> -1;  //diagonal left
            case 2 -> 0; //forward 1
            case 3 -> 0;  //forward 2
            default -> 0;
        };
    }

    private int getRowMod(int j, ChessGame.TeamColor myColor){
        int tempRowMod = switch (j){
            case 0 -> 1;  //diagonal right
            case 1 -> 1;  //diagonal left
            case 2 -> 1; //forward 1
            case 3 -> 2;  //forward 2
            default -> 0;
        };

        if(myColor == ChessGame.TeamColor.WHITE){
            tempRowMod = -tempRowMod;
        }

        return tempRowMod;
    }

    private void addMovesPromotions(ChessPosition tempPosition){
        legalMoves.add(new ChessMove(position, tempPosition, ChessPiece.PieceType.QUEEN));
        legalMoves.add(new ChessMove(position, tempPosition, ChessPiece.PieceType.BISHOP));
        legalMoves.add(new ChessMove(position, tempPosition, ChessPiece.PieceType.ROOK));
        legalMoves.add(new ChessMove(position, tempPosition, ChessPiece.PieceType.KNIGHT));
    }

    public ArrayList<ChessMove> calculateMoves(){

        int possibleMoves = 3;
        if(color == ChessGame.TeamColor.BLACK && row == 1  //checking if first move
                ||color == ChessGame.TeamColor.WHITE && row == 6){
            possibleMoves = 4;
        }

        for(int j = 0; j < possibleMoves; j++){ // 8 movement directions

            int rowMod = getRowMod(j, color);
            int colMod = getColMod(j);

            int tempRow = position.convertRowIndices(row + rowMod);
            int tempCol = position.convertColToChessIndices(col + colMod);
            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

//            System.out.println(tempPosition);

            if(checkBounds(tempRow, tempCol)){
                if(j < 2){ //diagonals
                    if(!checkEmpty(tempPosition) && checkEnemy(tempPosition)){
                        addMove(tempRow, tempPosition);
                    }
                }
                if(j > 1){ //forward
                    if(checkEmpty(tempPosition)){
                        addMove(tempRow, tempPosition);
                    }
                    else{
                        break; //don't check forward 2 if forward 1 was not empty
                    }
                }
            }


        }

        return legalMoves;
    }

    //helper function
    private void addMove(int tempRow, ChessPosition tempPosition){
        if (tempRow == 1 || tempRow == 8) {
            addMovesPromotions(tempPosition);
        }
        else {
            addMove(tempPosition);
        }
    }
}
