package ui;

import client.*;

import java.io.PrintStream;
import java.util.Scanner;

public class Repl {
    private Client client;
    private State state;

    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        GAMEPLAY
    }

    public Repl(){
        setState(State.LOGGED_OUT);
    }

    public void run(){
        PrintStream out = System.out;
        String response = "";

        setBlue(out);
        printWelcome(out);
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

    /// HELPER FUNCTIONS /////

    public void setState(State newState){
        this.state = newState;
        this.client = switch (newState){
            case LOGGED_OUT -> new ClientLoggedOut(this);
            case LOGGED_IN -> new ClientLoggedIn(this);
            case GAMEPLAY -> new ClientGameplay(this);
        };
    }

    private void printPrompt(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        out.printf("[%s]>>> ", state.toString());
        setDefault(out);
    }

    private void printWelcome(PrintStream out){
        out.printf("%s Welcome to Chess! %s%n",
                EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_QUEEN);
        out.println(client.help());
        out.println();
    }


    /// FORMATTING FUNCTIONS ///

    private void setBlue(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    private void setDefault(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }


}
