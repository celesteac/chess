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
        return "create";
    }

    private String list() {
        return "list";
    }

    private String play() {
        return "play";
    }

    private String observe() {
        return "observe";
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
