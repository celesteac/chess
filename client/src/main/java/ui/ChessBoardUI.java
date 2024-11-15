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
        drawRow();
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

    private void drawRow(){
        setBoarderColor();
        out.print(EscapeSequences.EMPTY);

        int squareColor = 0;
        for(int i = 0; i < BOARD_SIZE_IN_SQUARES; i++){
            switch (squareColor){
                case 0 -> setLightSquare();
                case 1 -> setDarkSquare();
            }

            out.print(EscapeSequences.EMPTY);

            squareColor = squareColor == 0 ? 1 : 0;
        }
        setBoarderColor();
        out.print(EscapeSequences.EMPTY);
        setDefaultColors();
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
