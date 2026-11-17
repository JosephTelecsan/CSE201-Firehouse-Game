package firehouse;

public enum Gear {
    TURNOUT_SUIT("turnout suit", "suit"),
    CROWBAR("crowbar", "bar"),
    LADDER("ladder"),
    KEYS("keys", "key");

    private final String label;
    private final String[] aliases;

    Gear(String label, String... aliases) {
        this.label = label;
        this.aliases = aliases;
    }

    public String label() {
        return label;
    }

    public static Gear fromCommand(String command) {
        for (Gear gear : values()) {
            if (command.equals(gear.label) || command.equals(gear.name().toLowerCase().replace('_', ' '))) {
                return gear;
            }
            for (String alias : gear.aliases) {
                if (command.equals(alias)) {
                    return gear;
                }
            }
        }
        return null;
    }
}
