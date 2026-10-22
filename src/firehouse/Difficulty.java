package firehouse;

public enum Difficulty {
    EASY("Easy", 60, 20, 2),
    NORMAL("Normal", 45, 15, 3),
    HARD("Hard", 30, 10, 4);

    private final String label;
    private final int shiftMinutes;
    private final int scenarioLimitMinutes;
    private final int victimsToRescue;

    Difficulty(String label, int shiftMinutes, int scenarioLimitMinutes, int victimsToRescue) {
        this.label = label;
        this.shiftMinutes = shiftMinutes;
        this.scenarioLimitMinutes = scenarioLimitMinutes;
        this.victimsToRescue = victimsToRescue;
    }

    public String label() {
        return label;
    }

    public int shiftMinutes() {
        return shiftMinutes;
    }

    public int scenarioLimitMinutes() {
        return scenarioLimitMinutes;
    }

    public int victimsToRescue() {
        return victimsToRescue;
    }

    public static Difficulty fromCommand(String command) {
        switch (command) {
            case "easy":
            case "1":
                return EASY;
            case "normal":
            case "2":
                return NORMAL;
            case "hard":
            case "3":
                return HARD;
            default:
                return null;
        }
    }
}
