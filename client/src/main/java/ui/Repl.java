package ui;

import client.*;

import java.util.Scanner;

public class Repl {
    private Client client;

    public Repl(){
        client = new ClientLoggedOut();
    }

    public void run(){
        System.out.printf("Welcome!%n%s%n", client.help());
        String response = "";

        while (!response.equals("quit")){
            System.out.printf(printPrompt());
            Scanner scanner = new Scanner(System.in);
            response = scanner.nextLine();
            System.out.printf("%s%n",client.eval(response));
        }
    }

    String printPrompt(){
        return ">>> ";
    }

    public String welcome(){
        return "Welcome!%n" + client.help();
    }

}
