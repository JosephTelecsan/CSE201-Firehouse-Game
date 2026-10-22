package firehouse;

public final class Game {
    private final TextEngine text;

    public Game(TextEngine text) {
        this.text = text;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printIntro();
            String command = text.readCommand("> ");
            if (command.isEmpty()) {
                continue;
            }
            switch (command) {
                case "1":
                case "new":
                case "new game":
                case "start":
                    startShift();
                    break;
                case "2":
                case "scores":
                case "high scores":
                    text.println("No shifts are recorded yet.");
                    break;
                case "3":
                case "exit":
                case "quit":
                    running = false;
                    break;
                case "help":
                    printMenuHelp();
                    break;
                default:
                    text.println("Unknown command. Type help for options.");
                    break;
            }
        }
        text.println("Shift desk closed.");
    }

    private void printIntro() {
        text.println("FIRE HOUSE");
        text.println("");
        text.println("You start at the firehouse. Gear up, take neighborhood calls, and finish the shift before time runs out.");
        text.println("");
        text.println("1) New game");
        text.println("2) High scores");
        text.println("3) Exit");
    }

    private void printMenuHelp() {
        text.println("Commands: new game, scores, help, exit");
        text.println("Difficulty: not selected");
        text.println("Time remaining: shift has not started");
    }

    /** Clock starts after difficulty is chosen. */
    private void startShift() {
        Difficulty difficulty = chooseDifficulty();
        if (difficulty == null) {
            return;
        }
        ShiftTimer timer = new ShiftTimer(difficulty.shiftMinutes());
        text.println("Shift started. Difficulty: " + difficulty.label()
                + ". Time remaining: " + timer.remainingMinutes() + " min.");
        text.println("Type help for commands.");
        while (!timer.isExpired()) {
            String command = text.readCommand("> ");
            if (command.isEmpty()) {
                continue;
            }
            timer.spendCommand();
            if (timer.isExpired()) {
                text.println("Shift failed. Time ran out.");
                return;
            }
            switch (command) {
                case "help":
                    printShiftHelp(difficulty, timer);
                    break;
                case "quit":
                case "exit":
                    text.println("You left the shift.");
                    return;
                default:
                    text.println("Unknown command. Type help for options.");
                    break;
            }
        }
    }

    private Difficulty chooseDifficulty() {
        text.println("Select difficulty: easy, normal, hard");
        while (true) {
            String command = text.readCommand("> ");
            if (command.isEmpty()) {
                continue;
            }
            if (command.equals("back") || command.equals("quit") || command.equals("exit")) {
                return null;
            }
            if (command.equals("help")) {
                text.println("Commands: easy, normal, hard, back");
                text.println("Difficulty: not selected");
                text.println("Time remaining: shift has not started");
                continue;
            }
            Difficulty difficulty = Difficulty.fromCommand(command);
            if (difficulty == null) {
                text.println("Choose easy, normal, or hard.");
                continue;
            }
            return difficulty;
        }
    }

    private void printShiftHelp(Difficulty difficulty, ShiftTimer timer) {
        text.println("Commands: help, quit");
        text.println("Difficulty: " + difficulty.label());
        text.println("Time remaining: " + timer.remainingMinutes() + " min");
        text.println("Scenario time limit: " + difficulty.scenarioLimitMinutes() + " min");
        text.println("Victims to rescue: " + difficulty.victimsToRescue());
    }
}
