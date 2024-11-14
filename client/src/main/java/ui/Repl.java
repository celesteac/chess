package ui;

import java.util.Scanner;

public class Repl {

    public void run(){
        System.out.println("Welcome!");
        System.out.printf(printPrompt());
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.printf("%nhello %s", line);

    }

    String printPrompt(){
        return "your name%n>>>";
    }
}
