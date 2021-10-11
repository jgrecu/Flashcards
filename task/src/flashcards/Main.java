package flashcards;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final List<Flashcard> flashcards = new ArrayList<>();
        //final Set<String> terms = new HashSet<>();
        //final Set<String> definitions = new HashSet<>();

        System.out.println("Input the number of cards:");
        int numberOfCards = Integer.parseInt(scanner.nextLine().strip());
        for (int i = 0; i < numberOfCards; i++) {
            String term = "";
            String definition = "";

            System.out.printf("Card #%d:%n", i + 1);
            while (true) {
                term = scanner.nextLine().strip();
                //if (terms.add(term)) {
                String finalTerm = term;
                if (flashcards.stream().noneMatch(o -> o.getTerm().equals(finalTerm))) {
                    break;
                } else {
                    System.out.printf("The term \"%s\" already exists. Try again:%n", term);
                }
            }
            System.out.printf("The definition for card #%d:%n", i + 1);
            while (true) {
                definition = scanner.nextLine().strip().toLowerCase();
                //if (definitions.add(definition)) {
                String finalDefinition = definition;
                if (flashcards.stream().noneMatch(o -> o.getDefinition().equals(finalDefinition))) {
                    break;
                } else {
                    System.out.printf("The definition \"%s\" already exists. Try again:%n", definition);
                }
            }
            flashcards.add(new Flashcard(term, definition));
        }

        for (Flashcard card : flashcards) {
            System.out.printf("Print the definition of \"%s\":%n", card.getTerm());
            String answer = scanner.nextLine().strip().toLowerCase();
            if (answer.equals(card.getDefinition())) {
                System.out.println("Correct!");
            } else {
                String tempTerm = flashcards.stream().filter(o -> o.getDefinition().equals(answer))
                        .map(Flashcard::getTerm).findFirst().orElse(null);
                if (tempTerm != null) {
                    System.out.printf("Wrong. The right answer is \"%s\", but your definition is correct for \"%s\".%n", card.getDefinition(), tempTerm);
                } else {
                    System.out.printf("Wrong. The right answer is \"%s\".%n", card.getDefinition());
                }
            }
        }
    }
}
