package client;

import chess.ChessBoard;
import chess.ChessGame;
import ui.ChessBoardPrint;
import ui.Repl;

import java.util.Arrays;

public class ClientGameplay implements Client{
    Repl ui;
    String serverUrl;
    ServerFacade serverFacade;
    String authtoken;
    WebSocketFacade wsFacade;

    public ClientGameplay(Repl repl, String serverUrl, String authtoken){
        this.ui = repl;
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authtoken = authtoken;
        this.wsFacade = new WebSocketFacade(serverUrl, repl);
        wsFacade.connect();
        drawBoard();
    }

    public String eval(String input){
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "help" -> help();
            case "quit" -> "please leave the game before quitting";
            case "leave" -> leave();
            case "resign" -> resign();
            case "move" -> move(params);
            case "highlight" -> highlight(params);
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

    private String move(String[] params){
        if(params.length == 2){
            wsFacade.makeMove();
            return "moving";
        }
        else if (params.length > 2) {
            throw new ResponseException(400, "Error: too may inputs");
        }
        else {
            throw new ResponseException(400, "Error: start or end position");
        }
    }

    private String resign(){
        wsFacade.resign();
        return "resigning";
    }

    private String highlight(String[] params){
        if(params.length == 1){
            return "highlighting";
        }
        else if (params.length > 1) {
            throw new ResponseException(400, "Error: too may inputs");
        }
        else {
            throw new ResponseException(400, "Error: missing piece position");
        }
    }

    private String leave(){
        wsFacade.leave();
        ui.setState(Repl.State.LOGGED_IN, authtoken);
        return "leaving game";
    }

    public String help(){
        return """
                Options:
                - help
                - move <start> <end>
                - redraw
                - highlight <position>
                - resign
                - leave""";
    }
}
