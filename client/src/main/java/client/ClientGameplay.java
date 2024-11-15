package client;

import ui.Repl;

public class ClientGameplay implements Client{
    Repl ui;
    String serverUrl;

    public ClientGameplay(Repl repl){
        this.ui = repl;
    }

    public String eval(String input){
        return switch (input) {
            //do something about the quit case
            case "help" -> help();
            case "quit" -> "quit";
            case "leave" -> leave();
            default -> help();
        };
    }

    private String leave(){
        ui.setState(Repl.STATE.LOGGED_IN);
        return "leaving game";
    }

    public String help(){
        return """
                - help
                - leave
                - reprint
                - quit
                """;
    }
}
