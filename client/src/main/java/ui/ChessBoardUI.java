package ui;

import chess.ChessBoard;

import java.io.PrintStream;

public class ChessBoardUI {
    private final PrintStream out = System.out;
    private final ChessBoard board;

    public ChessBoardUI(ChessBoard board){
        this.board = board;
    }

    public void drawBoard(){
//        setGray();
        out.print(board.toString());
    }

    private void setGray(){
        out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }
}
