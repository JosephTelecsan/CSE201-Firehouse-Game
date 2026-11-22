package firehouse;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;

// 
public final class Mission {	
	private final Map<String, Integer> peopleByRoom = new LinkedHashMap<>();
	private final Map<String, Room> rooms = new LinkedHashMap<>();
	private final Difficulty difficulty;
	private final String layoutName;
	private Room current;
	private final int totalPeople;
	
	public Mission(Difficulty difficulty) {
        this(difficulty, new Random());
    }

    public Mission(Difficulty difficulty, Random random) {
        this.difficulty = difficulty;
        this.totalPeople = difficulty.victimsToRescue();

        // Pick 1 of the 5 fixed house layouts at random
        int layoutIndex = random.nextInt(6);
        switch (layoutIndex) {
            case 0:
                this.layoutName = "6 Tanglewood Drive";
                buildTanglewood();
                break;
            case 1:
                this.layoutName = "13 Willow Street";
                buildWillow();
                break;
            case 2:
                this.layoutName = "42 Edgefield Road";
                buildEdgefield();
                break;
            case 3:
                this.layoutName = "10 Ridgeview Court";
                buildRidgeview();
                break;
            case 4:
                this.layoutName = "Nell's Diner";
                buildDiner();
                break;
            case 5:
                this.layoutName = "Grafton Drive";
                buildGrafton();
                break;
        }

        placePeopleRandomly(random);
    }

    // Building out the 5 house layouts

    private void buildTanglewood() {
        Room right_foyer = add(new Room("east_foyer", "Front Foyer", "Thick dark smoke is pouring out of the open front door behind you."));
        Room left_foyer = add(new Room("west_foyer", "Front Foyer", "As you get further from the door the smoke gets thicker."));
        Room living_room = add(new Room("living_room", "Living Room", "The smoke is thicker here, your vission narrows."));
        Room dining_room = add(new Room("dining_room", "Dining Room", "A collapsed table blocks part of the floor. The cieling creaks."));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room utility_room = add(new Room("utility_room", "Utility Room", "You can hear the car alarm blaring from the garage in front of you."));
        Room stairs = add(new Room("stairs", "Stairs", "(North to go down) You can't see the bottom of the staircase through the smoke." ));
        Room garage = add(new Room("garage", "Garage", "The car alarm is blaring, cans of spray paint and caulk burn in the corner."));
        Room hobby_room = add(new Room("hobby_room", "Hobby Room", "A computer monitor on the desk next to you is melting from the heat."));
        Room main_bathroom = add(new Room("main_bathroom", "Main Bathroom", "The toilet seat has begun to melt; hanging on the wall are burning towels."));
        Room nancys_room = add(new Room("nancys_room", "Nancy's Room", "Theres a doll in the corner of the room. Strangely, it seems ok."));
        Room master_bedroom = add(new Room("master_bedroom", "Master Bedroom", "The master bedroom of the house. The bed is on fire, dark smoke billowing from the flames."));
        Room ensuite = add(new Room("ensuite", "En Suite Bathroom", "Smoke from the bed is pouring into the bathroom, you can hardly see."));
        Room basement = add(new Room("basement", "Basement", "The breaker is at the bottom of the stairs, it sends sparks everywhere."));
        
        // Floorplan for the western part of the map
        linkBothWays(right_foyer, "west", left_foyer);
        linkBothWays(left_foyer, "south", hobby_room);
        linkBothWays(left_foyer, "west", main_bathroom);
        linkBothWays(left_foyer, "north", nancys_room);
        
        // Floorplan for the nothern part of the map
        linkBothWays(right_foyer, "north", living_room);
        linkBothWays(living_room, "west", master_bedroom);
        linkBothWays(master_bedroom, "north", ensuite);
        
        //Floorplan for the eastern part of the map
        linkBothWays(living_room, "east", stairs);
        linkBothWays(stairs, "north", basement);
        linkBothWays(stairs, "east", dining_room);
        linkBothWays(dining_room, "south", kitchen);
        linkBothWays(stairs, "south", utility_room);
        linkBothWays(utility_room, "south", garage);
        
        current = right_foyer;
    }

    private void buildWillow() {
        Room living_room = add(new Room("living_room", "Living Room", "Smoke pours of the open door behind you; infront of you is a burning couch."));
        Room gym = add(new Room("gym", "Gym", "The smoke is thick in the room, the treadmill is on fire."));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room studio = add(new Room("studio", "Studio", "The room is filled with smoke and the sound of crackling flames."));
        Room master_bedroom = add(new Room("master_bedroom", "Master Bedroom", "The master bedroom of the house. The bed is on fire, dark smoke billowing from the flames."));
        Room bathroom = add(new Room("bathroom", "Bathroom", "The toilet seat has begun to melt; hanging on the wall are burning towels."));
        Room south_hallway = add(new Room("hallway_south", "South Hallway", "A long, smoke-filled hallway connecting the rooms."));
        Room north_hallway = add(new Room("hallway_north", "North Hallway", "You travel further down the hallway, the smoke is thicker here."));
        Room basement_living_room = add(new Room("basement_living_room", "Basement Living Room", "The living room of the basement. There is less smoke down here."));
        Room basement_bar = add(new Room("basement_bar", "Basement Bar", "The bar area of the basement. Bottles of alcohol are spilt, feeding flames."));
        Room basement_utility = add(new Room("basement_utility", "Basement Utility Room", "The utility room of the basement. The water heater is on fire; sparks fly from the breaker"));
        Room stairs = add(new Room("stairs", "Stairs", "The stairs leading down to the basement. The smoke is thick, you can hardly see the bottom."));

        // Floorplan for the southern half of the first floor
        linkBothWays(living_room, "north", kitchen);
        linkBothWays(kitchen, "west", gym);
        linkBothWays(kitchen, "north", hallway_south);
        linkBothWays(hallway_south, "east", bathroom);
        linkBothWays(hallway_south, "west", stairs);

        // Floorplan for the northern half of the first floor
        linkBothWays(hallway_south, "north", hallway_north);
        linkBothWays(hallway_north, "east", studio);
        linkBothWays(hallway_north, "west", master_bedroom);

        // Floorplan for the basement
        linkBothWays(basement_living_room, "north", stairs);
        linkBothWays(stairs, "west", basement_utility);
        linkBothWays(basement_living_room, "east", basement_bar);
        linkBothWays(basement_bar, "north", basement_utility);

        current = living_room;
    }

    private void buildEdgefield() {
        Room hallway_south = add(new Room("hallway_south", "Southern Hallway", "Smoke pours of the open door behind you.The hallway is filled with smoke and heat."));
        Room hallway_mid = add(new Room("hallway_mid", "Central Hallway", "The further you move, the more smoke fills the air."));
        Room hallway_north = add(new Room("hallway_north", "Northern Hallway", "You move even further down the hallway, the window is covered in soot"));
        Room dining_room = add(new Room("dining_room", "Dining Room", "The long dining room table has collapsed, the ceiling creaks above you."));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room living_room = add(new Room("living_room", "Living Room", "The living room is filled with smoke and flames; the couch is on fire."));
        Room attic = add(new Room("attic", "Attic", "Stifling heat traps smoke under the pitched roof."));
        Room stairs_down = add(new Room("stairs_down", "Downstairs Stairs", "The stairs leading down to the basement. The smoke is thick, you can hardly see the bottom."));
        Room basement = add(new Room("basement", "Basement", "Cooler air lingers near the floor; smoke funnels up the steps."));
        Room garage = add(new Room("garage", "Garage", "The garage is filled with smoke and flames, the smell of gasoline burning fills your mask."));
        Room utility = add(new Room("utility", "Utility Room", "A small room containing the water heater and electrical panel. The air is thick with smoke."));
        Room bathroom = add(new Room("bathroom", "Bathroom", "The bathroom is filled with smoke and heat. The mirror is cracked and the tiles are stained."));
        Room back_room = add(new Room("back_room", "Back Room", "A back room of the basement, there is no smoke here, but the heat is intense."));

        //Floorplan for the southern half of the first floor
        linkBothWays(hallway_south, "north", hallway_mid);
        linkBothWays(hallway_south, "west", living_room);
        linkBothWays(hallway_mid, "north", hallway_north);
        linkBothWays(hallway_mid, "west", kitchen);

        // Floorplan for the northern half of the first floor
        linkBothWays(kitchen, "west", utility);
        linkBothWays(kitchen, "north", bathroom);
        linkBothWays(hallway_north, "east", dining_room);
        linkBothWays(dining_room, "south", garage);

        // Floorplan for the basement
        linkBothWays(hallway_mid, "east", stairs_down);
        linkBothWays(stairs_down, "south", basement);
        linkBothWays(basement, "west", back_room);
        
        current = hallway_south;
    }

    private void buildRidgeview() {
        Room foyer = add(new Room("foyer", "Foyer", "A tight entryway. Alarm sirens wail overhead."));
        Room dining_room = add(new Room("dining_room", "Dining Room", "Smoke fills the air. The table is on fire, the ceiling creaks."));
        Room kitchen = add(new Room("kitchen", "Kitchen", "Grease fire remnants hiss on the stove. Cabinets hang open."));
        Room living_room = add(new Room("living_room", "Living Room", "The living room is filled with smoke and flames; the couch is on fire."));
        Room basement = add(new Room("basement", "Basement", "The basement is filled with smoke and heat. The walls are damp and the floor is slippery."));
        Room master_bedroom = add(new Room("master_bedroom", "Master Bedroom", "The master bedroom of the house. The bed is on fire, smoke billowing."));
        Room walkin = add(new Room("walkin", "Walk-In Closet", "Racks of clothes smolder in the cramped space."));
        Room back_room = add(new Room("back_room", "Back Room", "A small room at the back of the closet. The smoke is thick and the heat is intense."));
        Room garage = add(new Room("garage", "Garage", "The garage is filled with smoke and flames; the cars are on fire."));
        Room bathroom = add(new Room("bathroom", "Bathroom", "The bathroom is filled with smoke and heat. The mirror is cracked and the tiles are stained."));
        Room utility = add(new Room("utility", "Utility Closet", "The water heater hisses loudly in the dark."));
        Room hallway = add(new Room("hallway", "Hallway", "A long hallway filled with smoke and heat. The floor creaks under your boots."));

        // Floorplan for the east half of the first floor
        linkBothWays(foyer, "east", kitchen);
        linkBothWays(kitchen, "east" , master_bedroom);
        linkBothWays(master_bedroom, "south", walkin);
        linkBothWays(walkin, "south", back_room);
        linkBothWays(dining_room, "north", basement);

        // Floorplan for the west half of the first floor
        linkBothWays(foyer, "west", dining_room);
        linkBothWays(dining_room, "south", living_room);
        linkBothWays(dining_room, "west", hallway);
        linkBothWays(hallway , "north", bathroom);
        linkBothWays(hallway, "south", utility);
        linkBothWays(hallway , "west", garage);

        current = foyer;
    }

    private void buildDiner() {
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
    
    private void buildGrafton() {
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

    // Helpers for constructing layouts
    
    private Room add(Room room) {
        rooms.put(room.id(), room);
        return room;
    }

    /** Links room a to room b using the direction FROM room A. */
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