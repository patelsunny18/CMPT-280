//Sunny Ka Patel
//SDK438
//11267665
//CMPT 280 - Assn. 1

import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;

public class Ship {

    // These are the name of the ships in Tractor Jack's fleet.  This is used ot initialize data for the assignemnt.
    // Note that it is a static variable, and not an instance variable.
    public static String ShipNames[] = { "The Prairie Onion", "The Blackstrap", "The Bunnyhug", "The Salty Farmer", "The Icebreaker" };

    // List of sacks of grain to be hauled by this ship.
    protected LinkedList280<Sack> cargo;

    // The name of this ship.
    protected String name;

    // The capacity (in pounds) of this ship.  A ship cannot carry more than this much grain.
    protected float capacity;

    /**
     * Create a new ship.
     *
     * @param name Name of ths hip.
     * @param capacity Capacity of the ship in pounds.  The ship can carry sacks of grain with total weight up to this value.
     */
    public Ship(String name, float capacity) {
        this.cargo = new LinkedList280<Sack>();
        this.name = name;
        this.capacity = capacity;
    }

    /**
     * Load a sack of grain onto this ship.
     * @param s The sack of grain to load.
     */
    public void loadSack(Sack s) {
        // Add the sack to the list of sacks this ship is carrying.  Since this is only a simulation, we don't
        // care if this causes the ship to be overloaded at the time of loading.
        cargo.insertFirst(s);
    }

    /**
     * Check whether this ship is overloaded.
     *
     * @return true if the total weight of all sacks of grain (of any type) on this ship exceeds this.capacity,
     *         false otherwise.
     * @timing O(n)
     */
    public boolean isOverloaded() {
        //iterator to iterate over the cargo of the ships
        LinkedIterator280<Sack> iterator280 = new LinkedIterator280<>(cargo);

        //variable to store the total weight
        int weight = 0;

        //
        while (iterator280.itemExists()) {
            weight += iterator280.item().getWeight();
            iterator280.goForth();
        }
        return weight > getCapacity();
    }

    /**
     * Determine how many sacks of a particular type of grain are on this ship.
     *
     * @param type A type of grain, e.g. WHEAT, BARLEY, RYE (See the enumerated type Grain in Sack.java)
     *
     * @return The number of sacks of grain on this ship with a grain type equal to 'type'.
     *
     * @timing O(n)
     */
    public int sacksOfGrainType(Grain type) {
        //iterator to iterate over the cargo of the ships
        LinkedIterator280<Sack> iterator280 = new LinkedIterator280<>(cargo);

        //variable to store the weight of given grain type
        int weight = 0;

        //adds the weight of given grain type; skips over every other type
        while (iterator280.itemExists()) {
            if (iterator280.item().getType() == type) {
                weight += iterator280.item().getWeight();
            }
            iterator280.goForth();
        }
        return weight;
    }

    // Accessor methods ...

    public LinkedList280<Sack> getCargo() {
        return cargo;
    }

    public String getName() {
        return name;
    }

    public float getCapacity() {
        return capacity;
    }

    /**
     *  @return a printable string describing the ship's name and contents.
     */
    public String toString() {
        return this.name + " has a " + this.capacity + " pound capacity and carries: " + cargo.toString()+'\n';
    }
}