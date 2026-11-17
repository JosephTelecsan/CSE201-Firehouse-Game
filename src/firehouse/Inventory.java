package firehouse;

import java.util.EnumSet;
import java.util.Set;

public final class Inventory {
    private final Set<Gear> items = EnumSet.noneOf(Gear.class);

    public boolean add(Gear gear) {
        return items.add(gear);
    }

    public boolean has(Gear gear) {
        return items.contains(gear);
    }

    public boolean hasAllRequired() {
        return items.containsAll(EnumSet.allOf(Gear.class));
    }

    public String summary() {
        if (items.isEmpty()) {
            return "empty";
        }
        StringBuilder builder = new StringBuilder();
        for (Gear gear : Gear.values()) {
            if (items.contains(gear)) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(gear.label());
            }
        }
        return builder.toString();
    }

    public String missingSummary() {
        StringBuilder builder = new StringBuilder();
        for (Gear gear : Gear.values()) {
            if (!items.contains(gear)) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(gear.label());
            }
        }
        return builder.length() == 0 ? "none" : builder.toString();
    }
}
