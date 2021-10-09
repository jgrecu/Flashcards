package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
//        System.out.println("\nCard:");
//        System.out.println("to go");
//        System.out.println("Definition:");
//        System.out.println("ir en Español");
        String term = scanner.nextLine();
        String definition = scanner.nextLine();
        String answer = scanner.nextLine();
        System.out.println("Your answer is " + (definition.equals(answer) ? "right!" : "wrong..."));
    }
}
