package client;

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
        } else {
            throw new ResponseException(400, "Error: missing game name");
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
        for (int i = 0; i < gamesMap.size(); i++) {
            GameDetails gameDetails = gamesMap.get(i);
            String whiteUsername = gameDetails.whiteUsername() == null ? "empty" : gameDetails.whiteUsername();
            String blackUsername = gameDetails.blackUsername() == null ? "empty" : gameDetails.blackUsername();

            sb.append(i).append(". ").append(gameDetails.gameName()).append("\n");
            sb.append("  White player: ").append(whiteUsername).append("\n");
            sb.append("  Black player: ").append(blackUsername).append("\n");
        }
        return sb.toString();
    }


    private String play(String[] params) {
        if (params.length == 2) {
            ui.setState(Repl.State.GAMEPLAY, authtoken);
            return "playing game " + params[0] + " as " + params[1];
        } else {
            throw new ResponseException(400, "Error: missing game or player color");
        }
    }

    private String observe(String[] params) {
        if (params.length == 1) {
            ui.setState(Repl.State.GAMEPLAY, authtoken);
            return "observing game " + params[0];
        } else {
            throw new ResponseException(400, "Error: missing game number");
        }
    }

    public String help() {
        return """
                Options:
                - help
                - logout
                - create <game name>
                - list
                - play <game number> <player color>
                - observe <game number>""";
    }

}
