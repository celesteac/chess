package client;

import chess.*;
import ui.ChessBoardPrint;
import ui.EscapeSequences;
import ui.Repl;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.*;

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
    ChessGame game;
    ChessBoard board;

    public ClientGameplay(Repl repl, String serverUrl, String authtoken, String username, int gameID, ChessGame.TeamColor playerColor){
        this.ui = repl;
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authtoken = authtoken;
        this.username = username;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.wsFacade = new WebSocketFacade(serverUrl, repl, this);
        this.game = new ChessGame();
        this.board = game.getBoard();

        UserGameCommand connectCommand = new UserGameCommand(type(CONNECT), authtoken, username,ClientGameplay.this.gameID);
        wsFacade.connect(connectCommand);
    }

    public void setGame(ChessGame game){
        this.game = game;
        this.board = game.getBoard();
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
            case "redraw" -> drawBoard(null);
            default -> help();
        };
    }

    public String drawBoard(Set<ChessPosition> highlightPositions){
        System.out.println();
        if(playerColor == null){
            new ChessBoardPrint(board, ChessGame.TeamColor.WHITE, highlightPositions).drawBoard();
        } else {
            new ChessBoardPrint(board, playerColor, highlightPositions).drawBoard();
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
            ChessPiece.PieceType promotionPiece = checkPawnPromotion(move);
            ChessMove moveWithPromotion = new ChessMove(move.getStartPosition(), move.getEndPosition(), promotionPiece);
            MakeMoveCommand moveCommand = new MakeMoveCommand(type(MAKE_MOVE), authtoken, username,gameID, moveWithPromotion);
            wsFacade.makeMove(moveCommand);
            return "moving...";
        }
        else if (params.length > 2) {
            throw new ResponseException(400, "Error: too may inputs");
        }
        else {
            throw new ResponseException(400, "Error: missing start or end position");
        }
    }

    private ChessPiece.PieceType checkPawnPromotion(ChessMove move){
        ChessPosition endPos = move.getEndPosition();
        int row = endPos.getRow();
        if(row == 0 || row == 7){
            ChessPosition startPos = move.getStartPosition();
            ChessMove hypotheticalMove = new ChessMove(startPos, endPos, ChessPiece.PieceType.QUEEN);
            Collection<ChessMove> validMoves = game.validMoves(startPos);
            if(validMoves.contains(hypotheticalMove)){
                return askPawnPromotion();
            }
        }
        return null;
    }

    private ChessPiece.PieceType askPawnPromotion(){
        System.out.println("Choose a promotion piece <QUEEN | KNIGHT | ROOK | BISHOP>");

        return ChessPiece.PieceType.QUEEN;
    }


    private String resign(){
        if(playerColor == null){
            throw new ResponseException(400, "Error: you are observing");
        }
        ui.notify(EscapeSequences.SET_TEXT_COLOR_BLUE + "Confirm you want to resign <yes | no>:");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        if(Objects.equals(response, "yes")) {
            try {
                UserGameCommand resignCommand = new UserGameCommand(type(RESIGN), authtoken, username, gameID);
                wsFacade.resign(resignCommand);
                return EscapeSequences.SET_TEXT_COLOR_BLUE + "resigning";
            } catch (ResponseException ex) {
                return "Error: " + ex.getMessage();
            }
        }
        else{
            return EscapeSequences.SET_TEXT_COLOR_BLUE + "Continuing game";
        }
    }

    private String highlight(String[] params){
        if(params.length == 1){
            String input = params[0];
            if(!isValidFormat(input)){
                throw new ResponseException(400, "Error: Please provide input of the form [a-h][1-8]");
            }
            ChessPosition position = getPosition(input);
            ChessPiece piece = board.getPiece(position);
            if(piece == null){
                throw new ResponseException(400, "Error: There is no piece at square " + position);
            }

            Set<ChessPosition> validPositions = getValidPositions(position);
            drawBoard(validPositions);

            if(validPositions.isEmpty()){
                return "No valid moves at " + position;
            } else {
                return "Showing valid moves at " + position;
            }
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



    /// HELPER FUNCTIONS /////


    public boolean isValidFormat(String str) {
        return str.matches("[a-h][1-8]");
    }

    private ChessMove getValidMove(String[] params){
        for(String param : params){
            if(!isValidFormat(param)){
                throw new ResponseException(400, "please provide moves in the format [a-h][1-8]");
            }
        }

        ChessPosition startPos = getPosition(params[0]);
        ChessPosition endPos = getPosition(params[1]);

        return new ChessMove(startPos, endPos, null);
    }

    private ChessPosition getPosition(String userInputPosition){
        String col = userInputPosition.substring(0,1);
        String row = userInputPosition.substring(1,2);
        int rowInt = Integer.parseInt(row);
        int colInt = getColNum(col);

        return new ChessPosition(rowInt, colInt);
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

    private Set<ChessPosition> getValidPositions(ChessPosition position){
        Collection<ChessMove> validMoves = game.validMoves(position);
        Set<ChessPosition> validPositions = new HashSet<>();

        for (ChessMove move : validMoves){
            validPositions.add(move.getEndPosition());
        }

        return validPositions;
    }


    private UserGameCommand.CommandType type(UserGameCommand.CommandType type) {
        return type;
    }
}
