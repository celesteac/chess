package client;

import ui.Repl;

public class ClientLoggedIn implements Client{
    Repl ui;
    String serverUrl;
    ServerFacade serverFacade;

    public ClientLoggedIn(Repl repl, String serverUrl){
        this.ui = repl;
        serverFacade = new ServerFacade(serverUrl);
    }

    public String eval(String input){
        return switch (input) {
            case "help" -> help();
            case "logout" -> logout();
            case "create" -> create();
            case "list" -> list();
            case "play" -> play();
            case "observe" -> observe();
            case "quit" -> "please logout before quitting";
            default -> help();
        };
    }

    private String logout() {
        ui.setState(Repl.State.LOGGED_OUT);
        return "logout";
    }

    private String create() {
        return "created new game";
    }

    private String list() {
        return "list of games";
    }

    private String play() {
        ui.setState(Repl.State.GAMEPLAY);
        return "playing game";
    }

    private String observe() {
        ui.setState(Repl.State.GAMEPLAY);
        return "observing game";
    }

    public String help(){
        return """
                Options:
                - help
                - logout
                - create
                - list
                - play
                - observe""";
    }

}
