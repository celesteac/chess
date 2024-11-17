package ui;

import client.*;

import java.io.PrintStream;
import java.util.Scanner;

public class Repl {
    String serverUrl;
    private Client client;
    private State state;

    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        GAMEPLAY
    }

    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
        setState(State.LOGGED_OUT);
    }

    public void run() {
        PrintStream out = System.out;
        String response = "";

        setBlue(out);
        printWelcome(out);
        out.print(EscapeSequences.moveCursorToLocation(0, 0));
        setDefault(out);

        while (!response.equals("quit")) {
            printPrompt(out);
            Scanner scanner = new Scanner(System.in);
            response = scanner.nextLine();

            setBlue(out);
            try {
                String output = client.eval(response);
                out.printf("%s", output);

                if(response.equals("quit") && state != State.LOGGED_OUT){
                    response = "";
                }
            } catch (Exception ex) {
                out.print(ex.getMessage());
            }

            setDefault(out);
            out.println();
        }
    }

    /// HELPER FUNCTIONS /////

    public void setState(State newState) {
        this.state = newState;
        this.client = switch (newState) {
            case LOGGED_OUT -> new ClientLoggedOut(this, serverUrl);
            case LOGGED_IN -> new ClientLoggedIn(this, serverUrl);
            case GAMEPLAY -> new ClientGameplay(this, serverUrl);
        };
    }

    private void printPrompt(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        out.printf("[%s]>>> ", state.toString());
        setDefault(out);
    }

    private void printWelcome(PrintStream out) {
        out.printf("%s Welcome to Chess! %s%n",
                EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_QUEEN);
        out.println(client.help());
        out.println();
    }


    /// FORMATTING FUNCTIONS ///

    private void setBlue(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    private void setDefault(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }


}
