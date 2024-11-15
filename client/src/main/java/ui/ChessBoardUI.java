package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

public class ChessBoardUI {
    private final PrintStream out = System.out;
    private final ChessBoard board;
    private final int BOARD_SIZE_IN_SQUARES = 8;
    private final int BOARDER_SIZE_IN_SQUARES = BOARD_SIZE_IN_SQUARES + 2;

    public ChessBoardUI(ChessBoard board){
        this.board = board;
    }

    public void drawBoard(){
        drawTopBoarder();
        drawRows();drawTopBoarder();
        out.print(board.toString());
    }

    private void drawTopBoarder(){
        setBoarderColor();
        setBoarderText();
        for(int i = 0; i < BOARDER_SIZE_IN_SQUARES; i++){
            out.print(EscapeSequences.EMPTY);
        }
        setDefaultColors();
        out.println();
    }

    private void drawRows(){
        int startingSquareColor = 0;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++){
            setBoarderColor();
            out.print(EscapeSequences.EMPTY);

            drawRow(startingSquareColor, i);
            setBoarderColor();

            out.print(EscapeSequences.EMPTY);
            setDefaultColors();
            out.println();
            startingSquareColor = startingSquareColor == 0 ? 1 : 0;
        }
    }

    private void drawRow(int startingSquareColor, int boardRow){
        int squareColor = startingSquareColor;
        for(int i = 0; i < BOARD_SIZE_IN_SQUARES; i++){
            switch (squareColor){
                case 0 -> setLightSquare();
                case 1 -> setDarkSquare();
            }

            out.print(getPieceUiString(getPieceAtPosition(boardRow,i)));
            squareColor = squareColor == 0 ? 1 : 0;
        }
    }

    private String getPieceUiString(ChessPiece piece){
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
        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
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
