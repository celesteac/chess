package client;

import ui.Repl;

public interface Client {
    public String help();
    public String eval(String input);
}
