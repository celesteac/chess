package client;

import ui.Repl;

public class ClientGameplay implements Client{
    Repl ui;
    String serverUrl;

    public ClientGameplay(Repl repl){
        this.ui = repl;
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public String eval(String input) {
        return "";
    }
}
