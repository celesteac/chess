package client;

import ui.Repl;

import java.util.Arrays;

public class ClientLoggedIn implements Client{
    Repl ui;
    ServerFacade serverFacade;
    String authtoken;

    public ClientLoggedIn(Repl repl, String serverUrl, String authtoken){
        this.ui = repl;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authtoken = authtoken;
    }

    public String eval(String input){
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
        if(params.length == 1) {
            return "created game " + params[0];
        }
        else {
            throw new ResponseException(400, "Error: missing game name");
        }
    }

    private String list() {
        return "list of games";
    }

    private String play(String[] params) {
        if(params.length == 2) {
            ui.setState(Repl.State.GAMEPLAY, authtoken);
            return "playing game " + params[0] + " as " + params[1] ;
        }
        else {
            throw new ResponseException(400, "Error: missing game or player color");
        }
    }

    private String observe(String[] params) {
        if(params.length == 1) {
            ui.setState(Repl.State.GAMEPLAY, authtoken);
            return "observing game " + params[0];
        }
        else {
            throw new ResponseException(400, "Error: missing game number");
        }
    }

    public String help(){
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
