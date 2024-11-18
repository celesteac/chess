package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

public class ChessBoardPrint {
    private final PrintStream out = System.out;
    private final ChessBoard board;
    private final int boardSizeInSquares = 8;
    private final ChessGame.TeamColor playerColor;

    public ChessBoardPrint(ChessBoard board, ChessGame.TeamColor playerColor){
        this.board = board;
        this.playerColor = playerColor;
    }

    public void drawBoard(){
        drawBoarder();
        drawRows();
        drawBoarder();
    }

    private void drawBoarder(){
        char[] columnLabels;
        if(playerColor == ChessGame.TeamColor.WHITE){
            char[] columnLabelsWhite = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
            columnLabels = columnLabelsWhite;
        } else {
            char[] columnLabelsBlack = {'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};
            columnLabels = columnLabelsBlack;
        }
        setBoarderColor();
        setBoarderText();
        out.print(EscapeSequences.EMPTY);
        for(int i = 0; i < boardSizeInSquares; i++){
            out.printf(" %c\u2003", columnLabels[i]);
        }
        out.print(EscapeSequences.EMPTY);
        setDefaultColors();
        out.println();
    }

    private void drawRows(){
        int startingSquareColor = 0;
        for (int i = 0; i < boardSizeInSquares; i++){
            drawBorderSquare(i);
            drawRow(startingSquareColor, i);
            drawBorderSquare(i);

            setDefaultColors();
            out.println();
            startingSquareColor = startingSquareColor == 0 ? 1 : 0;
        }
    }

    private void drawBorderSquare(int num){
        setBoarderColor();
        setBoarderText();
        if(playerColor == ChessGame.TeamColor.WHITE){
            num = 8 - num;
        }else{
            num = num +1;
        }
        out.printf(" %d\u2003", num);
    }

    private void drawRow(int startingSquareColor, int boardRow){
        int squareColor = startingSquareColor;
        if(playerColor == ChessGame.TeamColor.BLACK){
            boardRow = reverseIndex(boardRow);
        }

        for(int i = 0; i < boardSizeInSquares; i++){
            switch (squareColor){
                case 0 -> setLightSquare();
                case 1 -> setDarkSquare();
            }

            int boardColumn = i;
            if(playerColor == ChessGame.TeamColor.BLACK){
                boardColumn = reverseIndex(boardColumn);
            }

            out.print(getPieceAsString(getPieceAtPosition(boardRow,boardColumn)));
            squareColor = squareColor == 0 ? 1 : 0;
        }
    }

    private int reverseIndex(int index){
        return switch (index) {
            case 0 -> 7;
            case 1 -> 6;
            case 2 -> 5;
            case 3 -> 4;
            case 4 -> 3;
            case 5 -> 2;
            case 6 -> 1;
            case 7 -> 0;
            default -> -10; //error
        };
    }

    private String getPieceAsString(ChessPiece piece){
        if(piece == null){
            return EscapeSequences.EMPTY;
        }
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            setPlayerWhite();
            return switch (piece.getPieceType()){
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case PAWN -> EscapeSequences.WHITE_PAWN;
                case ROOK -> EscapeSequences.WHITE_ROOK;
            };
        } else {
            setPlayerBlack();
            return switch (piece.getPieceType()){
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case PAWN -> EscapeSequences.BLACK_PAWN;
                case ROOK -> EscapeSequences.BLACK_ROOK;
            };
        }
    }

    private ChessPiece getPieceAtPosition(int boardRow, int boardColumn){
        int row = ChessPosition.convertRowIndices(boardRow);
        int col = ChessPosition.convertColToChessIndices(boardColumn);
        ChessPosition position = new ChessPosition(row,col);
        return board.getPiece(position);
    }

    /// PRIVATE FORMATTING FUNCTIONS //////
    private void setDefaultColors(){
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        out.print(EscapeSequences.RESET_BG_COLOR);
    }

    private void setBoarderColor(){
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }

    private void setBoarderText(){
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private void setLightSquare(){
        out.print(EscapeSequences.SET_BG_COLOR_DARK_YELLOW);
    }

    private void setDarkSquare(){
        out.print(EscapeSequences.SET_BG_COLOR_BLUE);
    }

    private void setPlayerWhite(){
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private void setPlayerBlack(){
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }
}
