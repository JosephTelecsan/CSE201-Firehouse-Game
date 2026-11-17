package firehouse;

import java.util.LinkedHashMap;
import java.util.Map;

/** Station layout and gear placement for the pre-dispatch search. */
public final class FireStation {
    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private Room current;

    public FireStation() {
        Room bay = add(new Room("bay", "Apparatus Bay",
                "The truck bay. Hoses hang on the wall. The engine waits with its doors shut."));
        Room locker = add(new Room("locker", "Locker Room",
                "Rows of lockers and a bench. Turnout gear usually lives here."));
        Room office = add(new Room("office", "Duty Office",
                "A cluttered desk, a radio, and a key rack by the door."));
        Room supply = add(new Room("supply", "Supply Closet",
                "Shelves of tools, nozzles, and spare fittings."));
        Room kitchen = add(new Room("kitchen", "Kitchen",
                "A small kitchen off the bay. Coffee is still warm on the counter."));

        connect(bay, "north", locker);
        connect(bay, "east", office);
        connect(bay, "west", supply);
        connect(bay, "south", kitchen);
        connect(locker, "south", bay);
        connect(office, "west", bay);
        connect(supply, "east", bay);
        connect(kitchen, "north", bay);

        bay.place(Gear.LADDER);
        locker.place(Gear.TURNOUT_SUIT);
        office.place(Gear.KEYS);
        supply.place(Gear.CROWBAR);

        current = bay;
    }

    public Room current() {
        return current;
    }

    public boolean go(String direction) {
        String nextId = current.exits().get(direction);
        if (nextId == null) {
            return false;
        }
        current = rooms.get(nextId);
        return true;
    }

    public Gear take(Gear gear) {
        return current.take(gear);
    }

    private Room add(Room room) {
        rooms.put(room.id(), room);
        return room;
    }

    private static void connect(Room from, String direction, Room to) {
        from.link(direction, to.id());
    }
}
