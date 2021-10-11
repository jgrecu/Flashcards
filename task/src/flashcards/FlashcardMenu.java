package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlashcardMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Flashcard> flashcards;

    public FlashcardMenu() {
        this.flashcards = new ArrayList<>();
    }

    public void start() {
        run();
    }

    private void run() {
        boolean gameOn = true;
        while (gameOn) {
            System.out.println("\nInput the action (add, remove, import, export, ask, exit):");
            String option = scanner.nextLine().strip().toLowerCase();
            switch (option) {
                case "add":
                    addFlashcard();
                    break;
                case "remove":
                    removeFlashcard();
                    break;
                case "import":
                    importFromFile();
                    break;
                case "export":
                    exportToFile();
                    break;
                case "ask":
                    ask();
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    scanner.close();
                    gameOn = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void exportToFile() {
        System.out.println("File name:");
        String fileName = scanner.nextLine().strip();
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            int n = 0;
            for (Flashcard flashcard : flashcards) {
                printWriter.println(flashcard.getTerm() + ":" + flashcard.getDefinition());
                n++;
            }
            System.out.printf("%d cards have been saved.", n);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    private void importFromFile() {
        System.out.println("File name:");
        String fileName = scanner.nextLine().strip();
        File file = new File(fileName);
        try (Scanner sc = new Scanner(file)) {
            int n = 0;
            while (sc.hasNext()) {
                String[] card = sc.nextLine().strip().split(":");
                String term = card[0];
                String definition = card[1];
                if (flashcards.stream().anyMatch(o -> term.equals(o.getTerm()))) {
                    Flashcard flashcard = flashcards.stream().filter(o -> term.equals(o.getTerm()))
                            .findFirst().orElse(null);
                    flashcards.remove(flashcard);
                }
                flashcards.add(new Flashcard(term, definition));
                n++;
            }
            System.out.printf("%d cards have been loaded.%n", n);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    private void removeFlashcard() {
        System.out.println("Which card?");
        String term = scanner.nextLine().strip();
        if (flashcards.stream().anyMatch(o -> term.equals(o.getTerm()))) {
            Flashcard flashcard = flashcards.stream().filter(o -> term.equals(o.getTerm())).findFirst().orElse(null);
            flashcards.remove(flashcard);
            System.out.println("The card has been removed.");
        } else {
            System.out.printf("Can't remove \"%s\": there is no such card.", term);
        }
    }

    private void addFlashcard() {
        System.out.println("The card:");
        String term = scanner.nextLine().strip();
        String definition = "";

        if (flashcards.stream().anyMatch(o -> term.equals(o.getTerm()))) {
            System.out.printf("The card \"%s\" already exists.%n", term);
        } else {
            System.out.printf("The definition of the card:%n");
            definition = scanner.nextLine().strip();
            String finalDefinition = definition;
            if (flashcards.stream().anyMatch(o -> finalDefinition.equals(o.getDefinition()))) {
                System.out.printf("The definition \"%s\" already exists.%n", definition);
            } else {
                System.out.printf("The pair (\"%s\":\"%s\") has been added.%n", term, definition);
                flashcards.add(new Flashcard(term, definition));
            }
        }
    }

    private void ask() {
        System.out.println("How many times to ask?");
        int times = Integer.parseInt(scanner.nextLine().strip());
        for (int i = 0; i < times; i++) {
            System.out.printf("Print the definition of \"%s\":%n", flashcards.get(i).getTerm());
            String answer = scanner.nextLine().strip();
            if (answer.equals(flashcards.get(i).getDefinition())) {
                System.out.println("Correct!");
            } else {
                String tempTerm = flashcards.stream().filter(o -> o.getDefinition().equals(answer))
                        .map(Flashcard::getTerm).findFirst().orElse(null);
                if (tempTerm != null) {
                    System.out.printf("Wrong. The right answer is \"%s\", but your definition is correct for \"%s\".%n", flashcards.get(i).getDefinition(), tempTerm);
                } else {
                    System.out.printf("Wrong. The right answer is \"%s\".%n", flashcards.get(i).getDefinition());
                }
            }
        }
    }
}
