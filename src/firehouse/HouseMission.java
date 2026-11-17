package firehouse;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;

// 
public final class HouseMission {	
	private final Map<String, Integer> peopleByRoom = new LinkedHashMap<>();
	private final Map<String, Room> rooms = new LinkedHashMap<>();
	private final Difficulty difficulty;
	private final String layoutName;
	private Room current;
	private final int totalPeople;
	
	public HouseMission(Difficulty difficulty) {
        this(difficulty, new Random());
    }

    public HouseMission(Difficulty difficulty, Random random) {
        this.difficulty = difficulty;
        this.totalPeople = difficulty.victimsToRescue();

        // Pick 1 of the 5 fixed house layouts at random
        int layoutIndex = random.nextInt(5);
        switch (layoutIndex) {
            case 0:
                this.layoutName = "Suburban Two-Story";
                buildSuburbanTwoStory();
                break;
            case 1:
                this.layoutName = "Ranch";
                buildRanch();
                break;
            case 2:
                this.layoutName = "Manor";
                buildManor();
                break;
            case 3:
                this.layoutName = "Townhouse";
                buildTownhouse();
                break;
            default:
                this.layoutName = "Cabin";
                buildCabin();
                break;
        }

        placePeopleRandomly(random);
    }

    // Building out the 5 house layouts

    private void buildSuburbanTwoStory() {
        Room foyer = add(new Room("foyer", "Front Foyer", "Thick dark smoke curls down the main staircase. Leaking out of the open front door behind you."));
        Room living = add(new Room("living", "Living Room", "A couch smolders near the fireplace. Visibility is low."));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room dining = add(new Room("dining", "Dining Room", "A collapsed table blocks part of the floor. The cieling creaks."));
        Room hallUp = add(new Room("hall_up", "Upstairs Landing", "The floorboards creak under your boots. Smoke banks heavily near the ceiling."));
        Room bedroom1 = add(new Room("bedroom1", "Master Bedroom", "The master bedroom of the house. The bed is on fire, smoke billowing."));
        Room bedroom2 = add(new Room("bedroom2", "Kid's Bedroom", "Toys are scattered across the floor. A window is cracked open."));
        Room bath = add(new Room("bath", "Upstairs Bathroom", "Steam and smoke mix in the small tiled room."));
        Room garage = add(new Room("garage", "Attached Garage", "Fumes from paint cans and gasoline make the air sharp."));

        // Downstairs floor plan
        linkBothWays(foyer, "west", living);
        linkBothWays(foyer, "east", dining);
        linkBothWays(dining, "north", kitchen);
        linkBothWays(kitchen, "east", garage);

        // Upstairs floor plan
        linkBothWays(foyer, "north", hallUp);
        linkBothWays(hallUp, "west", bedroom1);
        linkBothWays(hallUp, "east", bedroom2);
        linkBothWays(hallUp, "north", bath);

        current = foyer;
    }

    private void buildRanch() {
        Room porch = add(new Room("porch", "Entry Hall","A narrow hallway leading deeper into the single-story ranch. Smoke hugs the ceiling."));
        Room famRoom = add(new Room("family_room", "Family Room", "A collapsed bookshelf leans against the wall. Books and board games spilled onto the floor."));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room sunroom = add(new Room("sunroom", "Sunroom", "Glass panes are cracked from the heat, letting in a draft."));
        Room hallway = add(new Room("hallway", "Bedroom Corridor", "A long, smoke-filled hallway connecting the sleeping quarters."));
        Room bedroom1 = add(new Room("bedroom1", "Master Bedroom", "The master bedroom of the house. The bed is on fire, smoke billowing."));
        Room guest = add(new Room("guest", "Guest Room", "The guest bedroom of the house. The carpet is on fire."));
        Room laundry = add(new Room("laundry", "Laundry Room", "The dryer is open, smoke billowing from within"));
        Room patio = add(new Room("patio", "Enclosed Patio", "Plastic furniture has begun to melt from the radiant heat."));

        // Main living wing
        linkBothWays(porch, "north", famRoom);
        linkBothWays(famRoom, "east", kitchen);
        linkBothWays(kitchen, "north", sunroom);
        linkBothWays(sunroom, "east", patio);

        // Bedroom wing
        linkBothWays(famRoom, "west", hallway);
        linkBothWays(hallway, "north", bedroom1);
        linkBothWays(hallway, "west", guest);
        linkBothWays(hallway, "south", laundry);

        current = porch;
    }

    private void buildManor() {
        Room vestibule = add(new Room("vestibule", "Grand Vestibule", "The small entrance is enclosed in fire. The air is thick with ash."));
        Room parlor = add(new Room("parlor", "Front Parlor", "Heavy curtains are ablaze along the windows."));
        Room study = add(new Room("study", "Library Study", "Burning papers drift through the air like embers."));
        Room dining = add(new Room("dining", "Formal Dining Hall", "A chandelier sways above a long mahogany table."));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room bedroom1 = add(new Room("bedroom1", "Master Bedroom", "The master bedroom of the house. The bed is on fire, smoke billowing."));
        Room attic = add(new Room("attic", "Attic", "Stifling heat traps smoke under the pitched roof."));
        Room bedroom2 = add(new Room("bedroom2", "Guest Room", "The bedframe has collapsed, wooden splinters all over the floor"));
        Room basement = add(new Room("basement", "basement", "Cooler air lingers near the floor; smoke funnels down the steps."));

        // downstairs floor plan
        linkBothWays(vestibule, "north", parlor);
        linkBothWays(parlor, "west", study);
        linkBothWays(parlor, "east", dining);
        linkBothWays(dining, "east", kitchen);
        linkBothWays(kitchen, "south", basement);

        // upstairs floor plan
        linkBothWays(parlor, "north", bedroom1);
        linkBothWays(bedroom1, "east", bedroom2);
        linkBothWays(bedroom1, "north", attic);

        current = vestibule;
    }

    private void buildTownhouse() {
        Room entry = add(new Room("entry", "Ground Entryway",
                "A tight entryway. Alarm sirens wail overhead."));
        Room openLiving = add(new Room("open_living", "Open Living Room",
                "Modern furniture burns fiercely in the open floor plan."));
        Room kitchen = add(new Room("kitchen", "Kitchen",
                "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room office = add(new Room("office", "Home Office",
                "Monitors and papers smolder on a burning desk."));
        Room stairs = add(new Room("stairs", "Second-Floor Landing",
                "Heat rises rapidly up the narrow stairwell."));
        Room bedroom1 = add(new Room("bedroom1", "Master Bedroom",
                "The master bedroom of the house. The bed is on fire, smoke billowing."));
        Room walkin = add(new Room("walkin", "Walk-In Closet",
                "Racks of clothes smolder in the cramped space."));
        Room balcony = add(new Room("balcony", "Rear Balcony",
                "Fresh air drafts in through a warped sliding door."));
        Room utility = add(new Room("utility", "Utility Closet",
                "The water heater hisses loudly in the dark."));

        // downstairs floor plan
        linkBothWays(entry, "north", openLiving);
        linkBothWays(openLiving, "east", kitchen);
        linkBothWays(kitchen, "south", utility);
        linkBothWays(openLiving, "west", balcony);

        // upstairs floor plan
        linkBothWays(openLiving, "north", stairs);
        linkBothWays(stairs, "west", office);
        linkBothWays(stairs, "east", bedroom1);
        linkBothWays(bedroom1, "north", walkin);

        current = entry;
    }

    private void buildCabin() {
        Room mudroom = add(new Room("mudroom", "Mudroom", "Thick woodsmoke fills the cabin."));
        Room openRoom = add(new Room("open_room", "Open Room", "A wooden coffee table burns ontop of a scortched rug"));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room loft = add(new Room("loft", "Loft", "Smoke banks heavily against the timber rafters."));
        Room bedroom1 = add(new Room("bedroom1", "Master Bedroom", "The master bedroom of the house. The bed is on fire, smoke billowing."));
        Room bunk = add(new Room("bunk", "Bunk Room", "Wooden bunk beds creak in the smoky haze."));
        Room pantry = add(new Room("pantry", "Pantry", "Shelves of canned goods rattle as timbers shift."));
        Room attic = add(new Room("attic", "Attic", "Stifling heat traps smoke under the pitched roof."));
        Room bath = add(new Room("bath", "bathroom", "A small bathroom with a frosted, heat-cracked window."));

        // downstairs floor plan
        linkBothWays(mudroom, "north", openRoom);
        linkBothWays(openRoom, "west", kitchen);
        linkBothWays(kitchen, "south", pantry);
        linkBothWays(openRoom, "east", bedroom1);
        linkBothWays(bedroom1, "south", bath);

        // upstairs floor plan
        linkBothWays(openRoom, "north", loft);
        linkBothWays(loft, "west", bunk);
        linkBothWays(loft, "north", attic);

        current = mudroom;
    }

    // Random person placement
    
    private void placePeopleRandomly(Random random) {
        // Gather all rooms except the entrance room so victims aren't at the front door
        List<Room> candidateRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (!room.id().equals(current.id())) {
                candidateRooms.add(room);
            }
        }

        for (int i = 0; i < totalPeople; i++) {
            Room chosenRoom = candidateRooms.get(random.nextInt(candidateRooms.size()));
            peopleByRoom.merge(chosenRoom.id(), 1, Integer::sum);
        }
    }

    // Navigation and gameplay helpers

    public Room current() {
        return current;
    }

    public String layoutName() {
        return layoutName;
    }

    public Difficulty difficulty() {
        return difficulty;
    }

    public int roomCount() {
        return rooms.size();
    }

    public int totalPeople() {
        return totalPeople;
    }

    public int peopleInCurrentRoom() {
        return peopleByRoom.getOrDefault(current.id(), 0);
    }

    /** Removes 1 victim from the current room, returning true if someone was rescued. */
    public boolean rescuePeopleInCurrentRoom() {
        int count = peopleInCurrentRoom();
        if (count <= 0) {
            return false;
        }
        if (count == 1) {
            peopleByRoom.remove(current.id());
        } else {
            peopleByRoom.put(current.id(), count - 1);
        }
        return true;
    }

    public int remainingPeople() {
        int remaining = 0;
        for (int count : peopleByRoom.values()) {
            remaining += count;
        }
        return remaining;
    }

    public boolean go(String direction) {
        String nextId = current.exits().get(direction);
        if (nextId == null) {
            return false;
        }
        current = rooms.get(nextId);
        return true;
    }

    private Room add(Room room) {
        rooms.put(room.id(), room);
        return room;
    }

    private static void linkBothWays(Room a, String directionFromA, Room b) {
        a.link(directionFromA, b.id());
        b.link(oppositeDirection(directionFromA), a.id());
    }

    private static String oppositeDirection(String direction) {
        switch (direction) {
            case "north": return "south";
            case "south": return "north";
            case "east":  return "west";
            case "west":  return "east";
            default: throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }
}