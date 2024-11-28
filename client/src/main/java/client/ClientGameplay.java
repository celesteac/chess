package client;

import chess.ChessBoard;
import chess.ChessGame;
import ui.ChessBoardPrint;
import ui.Repl;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.Arrays;

import static websocket.commands.UserGameCommand.CommandType.*;

public class ClientGameplay implements Client{
    Repl ui;
    String serverUrl;
    ServerFacade serverFacade;
    String authtoken;
    String username;
    int gameID;
    WebSocketFacade wsFacade;

    public ClientGameplay(Repl repl, String serverUrl, String authtoken, String username, int gameID){
        this.ui = repl;
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authtoken = authtoken;
        this.username = username;
        this.gameID = gameID;
        this.wsFacade = new WebSocketFacade(serverUrl, repl);

        UserGameCommand connectCommand = new UserGameCommand(type(CONNECT), authtoken, username,ClientGameplay.this.gameID); //fixme
        wsFacade.connect(connectCommand);
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
            UserGameCommand moveCommand = new UserGameCommand(type(MAKE_MOVE), authtoken, username,gameID); //fixme
            wsFacade.makeMove(moveCommand);
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
        try {
            UserGameCommand resignCommand = new UserGameCommand(type(RESIGN), authtoken, username, gameID); //fixme
            wsFacade.resign(resignCommand);
            return "resigning";
        } catch (ResponseException ex){
            return "Error: " + ex.getMessage();
        }
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
        UserGameCommand leaveCommand = new UserGameCommand(type(LEAVE), authtoken, username,gameID); //fixme
        wsFacade.leave(leaveCommand);
        ui.setState(Repl.State.LOGGED_IN, authtoken, username, null);
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

    private UserGameCommand.CommandType type(UserGameCommand.CommandType type) {
        return type;
    }
}
