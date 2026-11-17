package firehouse;

import java.util.Map;

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
        text.println("Find your gear in the firehouse before you embark.");
        text.println("Type help for commands.");
        boolean embarked = runFirehouse(difficulty, timer);
        if (embarked && !timer.isExpired()) {
        	runMission(difficulty, timer);
        	runMission(difficulty, timer);
        	runMission(difficulty, timer);
        }
    }
    
    private void runMission(Difficulty difficulty, ShiftTimer timer) {
    	Mission mission = new Mission(difficulty);
    	ShiftTimer scenarioTimer = new ShiftTimer(difficulty.scenarioLimitMinutes());
    	
    	text.println("");
    	text.println("!!!DISPATCH CALL!!!");
    	text.println("Structure: " + mission.layoutName());
    	text.println("Reports indicate " + mission.totalPeople() + " people trapped inside.");
    	text.println("Scene time limit: " + scenarioTimer.remainingMinutes() + " minutes.");
    	text.println("Type rescue when you find someone, and return to the entrance once clear.");
    	text.println("");
    	
    	String entranceId = mission.current().id();
    	lookHouse(mission);
    	
    	while (!timer.isExpired() && !scenarioTimer.isExpired()) {
            String command = text.readCommand("> ");
            if (command.isEmpty()) {
                continue;
            }
            timer.spendCommand();
            scenarioTimer.spendCommand();

            if (timer.isExpired() || scenarioTimer.isExpired()) {
                text.println("Mission failed. The fire overwhelmed the structure before you could finish.");
                return;
            }

            if (command.equals("help")) {
                printMissionHelp(mission, timer, scenarioTimer);
                continue;
            }
            if (command.equals("quit") || command.equals("exit")) {
                text.println("You aborted the rescue.");
                return;
            }
            if (command.equals("look") || command.equals("l")) {
                lookHouse(mission);
                continue;
            }
            if (command.startsWith("go ") || command.startsWith("move ")) {
                moveHouse(mission, command.substring(command.indexOf(' ') + 1).trim());
                continue;
            }
            if (normalizeDirection(command) != null) {
                moveHouse(mission, command);
                continue;
            }
            if (command.equals("rescue")) {
                rescuePeople(mission);
                if (mission.remainingPeople() == 0) {
                    text.println("All people accounted for! Head back to the entrance and type leave to finish.");
                }
                continue;
            }
            if (command.equals("leave")) {
                if (!mission.current().id().equals(entranceId)) {
                    text.println("You must return to the entrance before exiting the building.");
                } else if (mission.remainingPeople() > 0) {
                    text.println("There are still " + mission.remainingPeople() + " people trapped inside!");
                } else {
                    text.println("Mission complete! All " + mission.totalPeople()
                            + " people rescued with " + timer.remainingMinutes() + " shift min remaining.");
                    return;
                }
                continue;
            }

            text.println("Unknown command. Type help for options.");
        }
    }

    private void lookHouse(Mission mission) {
        Room room = mission.current();
        text.println(room.name());
        text.println(room.description());

        int peopleHere = mission.peopleInCurrentRoom();
        if (peopleHere == 1) {
            text.println("ALERT: You spot 1 trapped person here! Type 'rescue' to save them.");
        } else if (peopleHere > 1) {
            text.println("ALERT: You spot " + peopleHere + " trapped people here! Type 'rescue' to save one.");
        }

        StringBuilder exits = new StringBuilder("Exits: ");
        boolean firstExit = true;
        for (String direction : room.exits().keySet()) {
            if (!firstExit) {
                exits.append(", ");
            }
            exits.append(direction);
            firstExit = false;
        }
        text.println(exits.toString());
    }

    private void moveHouse(Mission mission, String rawDirection) {
        String direction = normalizeDirection(rawDirection);
        if (direction == null || !mission.go(direction)) {
            text.println("You cannot go that way.");
            return;
        }
        lookHouse(mission);
    }

    private void rescuePeople(Mission mission) {
        if (mission.rescuePeopleInCurrentRoom()) {
            int rescued = mission.totalPeople() - mission.remainingPeople();
            text.println("You guided a person to safety! (" + rescued + "/" + mission.totalPeople() + " rescued)");
        } else {
            text.println("There is no one here to rescue.");
        }
    }

    private void printMissionHelp(Mission mission, ShiftTimer shiftTimer, ShiftTimer scenarioTimer) {
        text.println("Commands: look, go <direction>, rescue, leave, help, quit");
        text.println("Directions: north, south, east, west");
        text.println("Victims remaining inside: " + mission.remainingPeople() + " / " + mission.totalPeople());
        text.println("Scene time remaining: " + scenarioTimer.remainingMinutes() + " min");
        text.println("Shift time remaining: " + shiftTimer.remainingMinutes() + " min");
    }
    
    private boolean runFirehouse(Difficulty difficulty, ShiftTimer timer) {
        FireStation station = new FireStation();
        Inventory inventory = new Inventory();
        look(station.current());

        while (!timer.isExpired()) {
            String command = text.readCommand("> ");
            if (command.isEmpty()) {
                continue;
            }
            timer.spendCommand();
            if (timer.isExpired()) {
                text.println("Shift failed. Time ran out.");
                return false;
            }

            if (command.equals("help")) {
                printFirehouseHelp(difficulty, timer, inventory);
                continue;
            }
            if (command.equals("quit") || command.equals("exit")) {
                text.println("You left the shift.");
                return false;
            }
            if (command.equals("look") || command.equals("l")) {
                look(station.current());
                continue;
            }
            if (command.equals("inventory") || command.equals("inv") || command.equals("i")) {
                text.println("Inventory: " + inventory.summary());
                continue;
            }
            if (command.startsWith("go ") || command.startsWith("move ")) {
                move(station, command.substring(command.indexOf(' ') + 1).trim());
                continue;
            }
            if (normalizeDirection(command) != null) {
                move(station, command);
                continue;
            }
            if (command.startsWith("take ") || command.startsWith("get ") || command.startsWith("grab ")) {
                String itemName = command.substring(command.indexOf(' ') + 1).trim();
                takeGear(station, inventory, itemName);
                continue;
            }
            if (command.equals("embark") || command.equals("leave") || command.equals("truck")
                    || command.equals("dispatch")) {
                if (!inventory.hasAllRequired()) {
                    text.println("You still need: " + inventory.missingSummary() + ".");
                } else {
                    text.println("All gear secured. Ready for shift dispatch.");
                    return true;
                }
                continue;
            }

            text.println("Unknown command. Type help for options.");
        }
        return false;
    }
    
    private void look(Room room) {
        text.println(room.name());
        text.println(room.description());
        if (room.gearHere().isEmpty()) {
            text.println("You see no gear here.");
        } else {
            StringBuilder gearLine = new StringBuilder("You see: ");
            boolean first = true;
            for (Gear gear : room.gearHere()) {
                if (!first) {
                    gearLine.append(", ");
                }
                gearLine.append(gear.label());
                first = false;
            }
            text.println(gearLine.toString());
        }
        StringBuilder exits = new StringBuilder("Exits: ");
        boolean firstExit = true;
        for (Map.Entry<String, String> exit : room.exits().entrySet()) {
            if (!firstExit) {
                exits.append(", ");
            }
            exits.append(exit.getKey());
            firstExit = false;
        }
        text.println(exits.toString());
    }

    private void takeGear(FireStation station, Inventory inventory, String itemName) {
        Gear gear = Gear.fromCommand(itemName);
        if (gear == null) {
            text.println("That is not essential gear.");
            return;
        }
        if (inventory.has(gear)) {
            text.println("You already have the " + gear.label() + ".");
            return;
        }
        if (station.take(gear) == null) {
            text.println("There is no " + gear.label() + " here.");
            return;
        }
        inventory.add(gear);
        text.println("Taken: " + gear.label() + ".");
        if (inventory.hasAllRequired()) {
            text.println("Gear complete. Type embark when you are ready to leave.");
        }
    }

    private void move(FireStation station, String rawDirection) {
        String direction = normalizeDirection(rawDirection);
        if (direction == null || !station.go(direction)) {
            text.println("You cannot go that way.");
            return;
        }
        look(station.current());
    }

    private static String normalizeDirection(String command) {
        switch (command) {
            case "n":
            case "north":
                return "north";
            case "s":
            case "south":
                return "south";
            case "e":
            case "east":
                return "east";
            case "w":
            case "west":
                return "west";
            default:
                return null;
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

    private void printFirehouseHelp(Difficulty difficulty, ShiftTimer timer, Inventory inventory) {
        text.println("Commands: look, go <direction>, take <gear>, inventory, embark, help, quit");
        text.println("Directions: north, south, east, west");
        text.println("Gear needed: turnout suit, crowbar, ladder, keys");
        text.println("Inventory: " + inventory.summary());
        text.println("Still need: " + inventory.missingSummary());
        text.println("Difficulty: " + difficulty.label());
        text.println("Time remaining: " + timer.remainingMinutes() + " min");
    }
}
