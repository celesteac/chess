package client;

import ui.Repl;

public class ClientLoggedOut implements Client {
    Repl ui;
    String serverUrl;
    ServerFacade serverFacade;

    public ClientLoggedOut(Repl repl, String serverUrl){
        this.ui = repl;
        serverFacade = new ServerFacade(serverUrl);
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
                Options:
                - help
                - login
                - register
                - quit""";
    }

    String login(){
        //authenticate
        ui.setState(Repl.State.LOGGED_IN);
        return "welcome to chess";
    }

}
