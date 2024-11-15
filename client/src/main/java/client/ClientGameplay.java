package client;

import chess.ChessBoard;
import ui.ChessBoardUI;
import ui.Repl;

public class ClientGameplay implements Client{
    Repl ui;
    String serverUrl;

    public ClientGameplay(Repl repl){
        this.ui = repl;
        drawBoard();
    }

    public String eval(String input){
        return switch (input) {
            //do something about the quit case
            case "help" -> help();
            case "quit" -> "quit";
            case "leave" -> leave();
            case "redraw" -> drawBoard();
            default -> "Unknown input. Options:%n" + help();
        };
    }

    private String drawBoard(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        new ChessBoardUI(board).drawBoard();
        return "drawing board";
    }

    private String leave(){
        ui.setState(Repl.State.LOGGED_IN);
        return "leaving game";
    }

    public String help(){
        return """
                - help
                - leave
                - redraw
                - quit
                """;
    }
}
