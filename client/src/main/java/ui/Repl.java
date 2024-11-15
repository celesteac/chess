package ui;

import client.*;

import java.io.PrintStream;
import java.util.Scanner;

public class Repl {
    private Client client;
    private STATE state;

    public enum STATE {
        LOGGED_OUT,
        LOGGED_IN,
        GAMEPLAY
    }

    public Repl(){
        setState(STATE.LOGGED_OUT);
    }

    public void run(){
        PrintStream out = System.out;
        String response = "";

        setBlue(out);
        out.printf("Welcome!%n%s%n", client.help());
        setDefault(out);

        while (!response.equals("quit")){
            printPrompt(out);
//            out.print(EscapeSequences.moveCursorToLocation(0,5));
            Scanner scanner = new Scanner(System.in);
            response = scanner.nextLine();

            setBlue(out);
            try {
                out.printf("%s", client.eval(response));
            } catch(Exception ex){
                out.print(ex.getMessage());
            }

            setDefault(out);
            out.println();
        }
    }

    private void printPrompt(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        out.printf("[%s]>>> ", state.toString());
        setDefault(out);
//        return "["+ state.toString() + "]>>> ";
    }

    private String welcome(){
        return "Welcome!%n" + client.help();
    }

    private void setBlue(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    private void setDefault(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    public void setState(STATE newState){
        this.state = newState;
        this.client = switch (newState){
            case LOGGED_OUT -> new ClientLoggedOut(this);
            case LOGGED_IN -> new ClientLoggedIn(this);
            case GAMEPLAY -> new ClientGameplay(this);
        };

    }

}
