package firehouse;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class Room {
    private final String id;
    private final String name;
    private final String description;
    private final Map<String, String> exits = new LinkedHashMap<>();
    private final Set<Gear> gearHere = new LinkedHashSet<>();

    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public void link(String direction, String roomId) {
        exits.put(direction, roomId);
    }

    public void place(Gear gear) {
        gearHere.add(gear);
    }

    public Gear take(Gear gear) {
        return gearHere.remove(gear) ? gear : null;
    }

    public Map<String, String> exits() {
        return Collections.unmodifiableMap(exits);
    }

    public Set<Gear> gearHere() {
        return Collections.unmodifiableSet(gearHere);
    }
}
