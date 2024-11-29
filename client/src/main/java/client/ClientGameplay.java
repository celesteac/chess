package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
    ChessGame.TeamColor playerColor;
    int gameID;
    WebSocketFacade wsFacade;
    ChessBoard board = new ChessBoard();

    public ClientGameplay(Repl repl, String serverUrl, String authtoken, String username, int gameID, ChessGame.TeamColor playerColor){
        this.ui = repl;
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authtoken = authtoken;
        this.username = username;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.wsFacade = new WebSocketFacade(serverUrl, repl, this);

        UserGameCommand connectCommand = new UserGameCommand(type(CONNECT), authtoken, username,ClientGameplay.this.gameID);
        wsFacade.connect(connectCommand);
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

    public void setBoard(ChessBoard board){
        this.board = board;
    }

    public String drawBoard(){
        System.out.println();
        if(playerColor == null){
            new ChessBoardPrint(board, ChessGame.TeamColor.WHITE).drawBoard();
        } else {
            new ChessBoardPrint(board, playerColor).drawBoard();
        }

        return "Drawing board";
    }

    private String move(String[] params){
        if(playerColor == null){
            throw new ResponseException(400, "Error: you are observing");
        }
        if(params.length == 2){
            //format letter[a-h]-num[1-8] x2
            //hacer un move
            ChessMove move = getValidMove(params);
            MakeMoveCommand moveCommand = new MakeMoveCommand(type(MAKE_MOVE), authtoken, username,gameID, move);
            wsFacade.makeMove(moveCommand);
            return "moving";
        }
        else if (params.length > 2) {
            throw new ResponseException(400, "Error: too may inputs");
        }
        else {
            throw new ResponseException(400, "Error: missing start or end position");
        }
    }

    public boolean isValidFormat(String str) {
        return str.matches("[a-h][1-8]");
    }

    private ChessMove getValidMove(String[] params){
        for(String param : params){
            if(!isValidFormat(param)){
                throw new ResponseException(400, "please provide moves in the format [a-h][1-8]");
            }
        }
        String start = params[0];
        String end = params[1];
        String startCol = start.substring(0,1);
        String startRow = start.substring(1,2);
        String endCol = end.substring(0,1);
        String endRow = end.substring(1,2);
        int startRowInt = Integer.parseInt(startRow);
        int startColInt = getColNum(startCol);
        int endRowInt = Integer.parseInt(endRow);
        int endColInt = getColNum(endCol);

        ChessPosition startPos = new ChessPosition(startRowInt, startColInt);
        ChessPosition endPos = new ChessPosition(endRowInt, endColInt);

        return new ChessMove(startPos, endPos, null);
    }

    private int getColNum(String colLetter){
        return switch (colLetter){
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> -10; //error
        };
    }

    private String resign(){
        if(playerColor == null){
            throw new ResponseException(400, "Error: you are observing");
        }
        try {
            UserGameCommand resignCommand = new UserGameCommand(type(RESIGN), authtoken, username, gameID);
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
        UserGameCommand leaveCommand = new UserGameCommand(type(LEAVE), authtoken, username,gameID);
        wsFacade.leave(leaveCommand);
        ui.setState(Repl.State.LOGGED_IN, authtoken, username, null, null);
        return "leaving game";
    }

    public String help(){
        if(playerColor == null){
            return """
                Options:
                - help
                - redraw
                - highlight <position>
                - leave""";

        }
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
