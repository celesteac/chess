package client;

import model.AuthData;
import ui.Repl;

import java.util.Arrays;

public class ClientLoggedOut implements Client {
    Repl ui;
    ServerFacade serverFacade;

    public ClientLoggedOut(Repl repl, String serverUrl){
        this.ui = repl;
        serverFacade = new ServerFacade(serverUrl);
    }

    public String eval(String input){
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "help" -> help();
            case "login" -> login(params);
            case "register" -> register(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String help(){
        return """
                Options:
                - help
                - login <username> <password>
                - register <username> <password> <email>
                - quit""";
    }

    String login(String[] params) throws ResponseException {
        if(params.length == 2) {
            String username = params[0];
            String password = params[1];
            AuthData auth = serverFacade.login(username, password);
            ui.setState(Repl.State.LOGGED_IN, auth.authToken(), username, null, null);
            return "Welcome to chess, " + username;
        }
        else if(params.length < 2) {
            throw new ResponseException(400, "Error: missing username or password");
        }
        else {
            throw new ResponseException(400, "Error: too many inputs");
        }
    }

    String register(String[] params) throws ResponseException {
        if(params.length == 3) {
            String username = params[0];
            AuthData auth = serverFacade.register(username, params[1], params[2]);
            ui.setState(Repl.State.LOGGED_IN, auth.authToken(), username, null, null);
            return "Welcome to chess, " + username + "!";
        }
        else if (params.length < 3){
            throw new ResponseException(400, "Error: missing user input");
        }
        else {
            throw new ResponseException(400, "Error: too many inputs");
        }
    }

}
