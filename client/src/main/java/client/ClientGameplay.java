package client;

import chess.ChessBoard;
import chess.ChessGame;
import ui.ChessBoardPrint;
import ui.Repl;

public class ClientGameplay implements Client{
    Repl ui;
    ServerFacade serverFacade;
    String authtoken;

    public ClientGameplay(Repl repl, String serverUrl, String authtoken){
        this.ui = repl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authtoken = authtoken;
        drawBoard();
    }

    public String eval(String input){
        return switch (input) {
            //do something about the quit case
            case "help" -> help();
            case "quit" -> "please leave the game before quitting";
            case "leave" -> leave();
            case "redraw" -> drawBoard();
            default -> help();
        };
    }

    private String drawBoard(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        new ChessBoardPrint(board, ChessGame.TeamColor.BLACK).drawBoard();
        System.out.println();
        new ChessBoardPrint(board, ChessGame.TeamColor.WHITE).drawBoard();
        return "drawing board";
    }

    private String leave(){
        ui.setState(Repl.State.LOGGED_IN, authtoken);
        return "leaving game";
    }

    public String help(){
        return """
                Options:
                - help
                - leave
                - redraw""";
    }
}
