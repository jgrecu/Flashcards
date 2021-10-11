package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class FlashcardMenu {
    public static final String FILE_NOT_FOUND = "File not found.";
    public static final String FILE_NAME = "File name:";
    private final Scanner scanner = new Scanner(System.in);
    private final List<Flashcard> flashcards;
    private final List<String> logs;
    private final Map<String, String> argMap;

    public FlashcardMenu() {
        this.flashcards = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.argMap = new HashMap<>();
    }

    public void start(String[] args) {
        mapArgs(args);
        if (argMap.containsKey("-import")) {
            importFromFile(argMap.get("-import"));
        }
        run();
    }

    private void mapArgs(String[] args) {
        for (int i = 1; i < args.length; i++) {
            argMap.put(args[i - 1].toLowerCase(), args[i]);
        }
    }

    private void run() {
        boolean gameOn = true;
        while (gameOn) {
            printMessage("\nInput the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String option = getInput().toLowerCase();
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
                    printMessage("Bye bye!");
                    if (argMap.containsKey("-export")) {
                        exportToFile(argMap.get("-export"));
                    }
                    scanner.close();
                    gameOn = false;
                    break;
                case "log":
                    saveLog();
                    break;
                case "hardest card":
                    printHardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                default:
                    break;
            }
        }
    }

    private void resetStats() {
        flashcards.forEach(o -> o.setWrongAnswers(0));
        printMessage("Card statistics have been reset.");
    }

    private void printHardestCard() {
        int max =  flashcards.stream().map(Flashcard::getWrongAnswers).max(Integer::compareTo).orElse(0);

        if (max == 0) {
            printMessage("There are no cards with errors.");
        } else {
            int finalMax = max;
            ArrayList<String> maxCards = flashcards.stream()
                    .filter(o -> o.getWrongAnswers() == finalMax)
                    .map(Flashcard::getTerm).collect(Collectors.toCollection(ArrayList::new));
            if (maxCards.size() > 1) {
                String output1 = "The hardest cards are \"" + String.join("\", \"", maxCards) + "\". You have " +
                        max + " errors answering them.";
                printMessage(output1);
            } else {
                String output2 = "The hardest card is \"" + String.join("\", \"", maxCards) + "\". You have " +
                        max + " errors answering it.";
                printMessage(output2);
            }
        }
    }

    private void saveLog() {
        printMessage(FILE_NAME);
        String fileName = getInput();
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String log : logs) {
                printWriter.println(log);
            }
            printMessage("The log has been saved.");
        } catch (FileNotFoundException e) {
            printMessage(FILE_NOT_FOUND);
        }
    }

    private void exportToFile() {
        printMessage(FILE_NAME);
        String fileName = getInput();
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            int n = 0;
            for (Flashcard flashcard : flashcards) {
                printWriter.println(flashcard.getTerm() + ":" + flashcard.getDefinition() + ":"
                        + flashcard.getWrongAnswers());
                n++;
            }
            String output = String.format("%d cards have been saved.", n);
            printMessage(output);
        } catch (FileNotFoundException e) {
            printMessage(FILE_NOT_FOUND);
        }
    }

    private void exportToFile(String fileName) {
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            int n = 0;
            for (Flashcard flashcard : flashcards) {
                printWriter.println(flashcard.getTerm() + ":" + flashcard.getDefinition() + ":"
                        + flashcard.getWrongAnswers());
                n++;
            }
            String output = String.format("%d cards have been saved.", n);
            printMessage(output);
        } catch (FileNotFoundException e) {
            printMessage(FILE_NOT_FOUND);
        }
    }

    private void importFromFile() {
        printMessage(FILE_NAME);
        String fileName = getInput();
        File file = new File(fileName);
        try (Scanner sc = new Scanner(file)) {
            int n = 0;
            while (sc.hasNext()) {
                String[] card = sc.nextLine().strip().split(":");
                String term = card[0];
                String definition = card[1];
                int wrongAnswers = Integer.parseInt(card[2]);
                if (flashcards.stream().anyMatch(o -> term.equals(o.getTerm()))) {
                    Flashcard flashcard = flashcards.stream().filter(o -> term.equals(o.getTerm()))
                            .findFirst().orElse(null);
                    flashcards.remove(flashcard);
                }
                Flashcard newFlashcard = new Flashcard(term, definition);
                newFlashcard.setWrongAnswers(wrongAnswers);
                flashcards.add(newFlashcard);
                n++;
            }
            String output = String.format("%d cards have been loaded.", n);
            printMessage(output);
        } catch (FileNotFoundException e) {
            printMessage(FILE_NOT_FOUND);
        }
    }

    private void importFromFile(String fileName) {
        File file = new File(fileName);
        try (Scanner sc = new Scanner(file)) {
            int n = 0;
            while (sc.hasNext()) {
                String[] card = sc.nextLine().strip().split(":");
                String term = card[0];
                String definition = card[1];
                int wrongAnswers = Integer.parseInt(card[2]);
                if (flashcards.stream().anyMatch(o -> term.equals(o.getTerm()))) {
                    Flashcard flashcard = flashcards.stream().filter(o -> term.equals(o.getTerm()))
                            .findFirst().orElse(null);
                    flashcards.remove(flashcard);
                }
                Flashcard newFlashcard = new Flashcard(term, definition);
                newFlashcard.setWrongAnswers(wrongAnswers);
                flashcards.add(newFlashcard);
                n++;
            }
            String output = String.format("%d cards have been loaded.", n);
            printMessage(output);
        } catch (FileNotFoundException e) {
            printMessage(FILE_NOT_FOUND);
        }
    }

    private void removeFlashcard() {
        printMessage("Which card?");
        String term = getInput();
        if (flashcards.stream().anyMatch(o -> term.equals(o.getTerm()))) {
            Flashcard flashcard = flashcards.stream().filter(o -> term.equals(o.getTerm()))
                    .findFirst().orElse(null);
            flashcards.remove(flashcard);
            printMessage("The card has been removed.");
        } else {
            String output = String.format("Can't remove \"%s\": there is no such card.", term);
            printMessage(output);
        }
    }

    private void addFlashcard() {
        printMessage("The card:");
        String term = getInput();
        String definition = "";

        if (flashcards.stream().anyMatch(o -> term.equals(o.getTerm()))) {
            String output = String.format("The card \"%s\" already exists.", term);
            printMessage(output);
        } else {
            printMessage("The definition of the card:");
            definition = getInput();
            String finalDefinition = definition;
            if (flashcards.stream().anyMatch(o -> finalDefinition.equals(o.getDefinition()))) {
                String output = String.format("The definition \"%s\" already exists.", definition);
                printMessage(output);
            } else {
                String output = String.format("The pair (\"%s\":\"%s\") has been added.", term, definition);
                printMessage(output);
                flashcards.add(new Flashcard(term, definition));
            }
        }
    }

    private void ask() {
        printMessage("How many times to ask?");
        int times = Integer.parseInt(getInput());
        for (int i = 0; i < times; i++) {
            String output = String.format("Print the definition of \"%s\":", flashcards.get(i).getTerm());
            printMessage(output);
            String answer = getInput();
            if (answer.equals(flashcards.get(i).getDefinition())) {
                printMessage("Correct!");
            } else {
                String tempTerm = flashcards.stream().filter(o -> o.getDefinition().equals(answer))
                        .map(Flashcard::getTerm).findFirst().orElse(null);
                if (tempTerm != null) {
                    String output1 = String.format(
                            "Wrong. The right answer is \"%s\", but your definition is correct for \"%s\".",
                            flashcards.get(i).getDefinition(), tempTerm);
                    flashcards.get(i).addWrongAnswer();
                    printMessage(output1);
                } else {
                    String output2 = String.format("Wrong. The right answer is \"%s\".",
                            flashcards.get(i).getDefinition());
                    flashcards.get(i).addWrongAnswer();
                    printMessage(output2);
                }
            }
        }
    }

    private void printMessage(String message) {
        logs.add(message);
        System.out.println(message);
    }

    private String getInput() {
        String message = scanner.nextLine().strip();
        logs.add(message);
        return message;
    }
}
