package client;

import ui.Repl;

public class ClientLoggedIn implements Client{
    Repl ui;
    String serverUrl;

    public ClientLoggedIn(Repl repl){
        this.ui = repl;
    }

    public String eval(String input){
        return switch (input) {
            case "help" -> help();
            case "logout" -> logout();
            case "create" -> create();
            case "list" -> list();
            case "play" -> play();
            case "observe" -> observe();
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String logout() {
        ui.setState(Repl.STATE.LOGGED_OUT);
        return "logout";
    }

    private String create() {
        return "created new game";
    }

    private String list() {
        return "list of games";
    }

    private String play() {
        ui.setState(Repl.STATE.GAMEPLAY);
        return "playing game";
    }

    private String observe() {
        ui.setState(Repl.STATE.GAMEPLAY);
        return "observing game";
    }

    public String help(){
        return """
                - help
                - logout
                - create
                - list
                - play
                - observe
                - quit
                """;
    }

}
