package ui;

import chess.ChessBoard;

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
        out.print(board.toString());
    }

    private void drawTopBoarder(){
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        for(int i = 0; i < BOARDER_SIZE_IN_SQUARES; i++){
            out.print(EscapeSequences.EMPTY);
        }
        setDefaultColors();
        out.println();
    }

    private void drawRows(){

    }

    private void setDefaultColors(){
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        out.print(EscapeSequences.RESET_BG_COLOR);
    }

    private void setGray(){
        out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }
}
