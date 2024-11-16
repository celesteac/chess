import client.ServerFacade;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        String serverUrl = "";
        Repl repl = new Repl(serverUrl);
        repl.run();
    }
}