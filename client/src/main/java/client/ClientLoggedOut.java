package client;

import ui.Repl;

public class ClientLoggedOut implements Client {
    Repl ui;
    String serverUrl;

    public ClientLoggedOut(Repl repl){
        this.ui = repl;
    }

    public String eval(String input){
        return switch (input) {
            case "help" -> help();
            case "login" -> login();
            case "register" -> login();
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String help(){
        return """
                - help
                - login
                - register
                - quit
                """;
    }

    String login(){
        //authenticate
        ui.setState(Repl.State.LOGGED_IN);
        return "welcome to chess";
    }

}
