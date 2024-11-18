package client;

import chess.ChessGame;
import model.GameData;
import requestresponsetypes.*;
import ui.Repl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientLoggedIn implements Client {
    Repl ui;
    ServerFacade serverFacade;
    String authtoken;
    Map<Integer, GameDetails> gamesMap;

    public ClientLoggedIn(Repl repl, String serverUrl, String authtoken) {
        this.ui = repl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authtoken = authtoken;
    }

    public String eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "help" -> help();
            case "logout" -> logout();
            case "create" -> create(params);
            case "list" -> list();
            case "play" -> play(params);
            case "observe" -> observe(params);
            case "quit" -> "please logout before quitting";
            default -> help();
        };
    }

    private String logout() {
        serverFacade.logout(authtoken);
        ui.setState(Repl.State.LOGGED_OUT, null);
        return "logging out";
    }

    private String create(String[] params) {
        if (params.length == 1) {
            serverFacade.createGame(params[0], authtoken);
            return "created game " + params[0];
        }
        else if(params.length < 1) {
            throw new ResponseException(400, "Error: missing game name");
        }
        else {
            throw new ResponseException(400, "Error: too many inputs");
        }
    }

    private String list() {
        ArrayList<GameDetails> gamesList = serverFacade.listGames(authtoken);
        setGamesMap(gamesList);
        return gamesMapToString(this.gamesMap);
    }

    private void setGamesMap(ArrayList<GameDetails> gameList) {
        Map<Integer, GameDetails> gamesMap = new HashMap<>();
        for (int i = 0; i < gameList.size(); i++) {
            GameDetails gameDetails = gameList.get(i);
            gamesMap.put(i + 1, gameDetails);
        }
        this.gamesMap = gamesMap;
    }

    private String gamesMapToString(Map<Integer, GameDetails> gamesMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("Games:").append("\n");
        for (int i = 0; i < gamesMap.size(); i++) {
            int gameNum = i+1;
            GameDetails gameDetails = gamesMap.get(gameNum);
            String whiteUsername = gameDetails.whiteUsername() == null ? "empty" : gameDetails.whiteUsername();
            String blackUsername = gameDetails.blackUsername() == null ? "empty" : gameDetails.blackUsername();

            sb.append(gameNum).append(". ").append(gameDetails.gameName()).append("\n");
            sb.append("   White player: ").append(whiteUsername).append("\n");
            sb.append("   Black player: ").append(blackUsername).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    private String play(String[] params) {
        if(gamesMap == null){
            throw new ResponseException(400, "Error: please list games first");
        }
        if (params.length == 2) {
            try{
                int gameNum = Integer.parseInt(params[0]);
                ChessGame.TeamColor playerColor = getPlayerColor(params[1]);
                GameDetails game = gamesMap.get(gameNum);
                if(game == null){
                    throw new ResponseException(400, "Error: no game number " + gameNum);
                }

                serverFacade.joinGame(game.gameID(), playerColor, authtoken);
                ui.setState(Repl.State.GAMEPLAY, authtoken);
                return "Playing game " + params[0] + " \"" + game.gameName() + "\" as " + params[1];

            } catch (NumberFormatException ex){
                throw new ResponseException(400, "Error: please provide the game number");
            }
        }
        else if(params.length < 2) {
            throw new ResponseException(400, "Error: missing game or player color");
        }
        else {
            throw new ResponseException(400, "Error: too many inputs");
        }
    }

    private ChessGame.TeamColor getPlayerColor(String color) throws ResponseException{
        if(color.equalsIgnoreCase("WHITE")){
            return ChessGame.TeamColor.WHITE;
        }
        if(color.equalsIgnoreCase("BLACK")){
            return ChessGame.TeamColor.BLACK;
        }
        else{
            throw new ResponseException(400, "Error: please provide color WHITE or BLACK");
        }
    }

    private String observe(String[] params) { //see what happens if they don't list the game first
        if(gamesMap == null){
            throw new ResponseException(400, "Error: please list games first");
        }
        if (params.length == 1) {
            try{
                int gameNum = Integer.parseInt(params[0]);
                GameDetails game = gamesMap.get(gameNum);
                if(game == null){
                    throw new ResponseException(400, "Error: no game number " + gameNum);
                }

//                serverFacade.joinGame(game.gameID(), null, authtoken);
                ui.setState(Repl.State.GAMEPLAY, authtoken);
                return "Observing game " + gameNum + " \"" + game.gameName() + "\"";

            } catch (NumberFormatException ex){
                throw new ResponseException(400, "Error: please provide the game number");
            }
        }
        else if(params.length < 1) {
            throw new ResponseException(400, "Error: missing game number");
        }
        else{
            throw new ResponseException(400, "Error: too many inputs");
        }
    }

    public String help() {
        return """
                Options:
                - help
                - logout
                - create <game name>
                - list
                - play <game number> <WHITE | BLACK>
                - observe <game number>""";
    }

}
