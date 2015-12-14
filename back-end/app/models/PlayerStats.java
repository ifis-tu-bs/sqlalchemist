package models;

import javax.persistence.Embeddable;

/**
 *  PlayerState Class
 *
 *  @version 1.0
 *  @author fabiomazzone
 */
@Embeddable
public class PlayerStats {
    private int health;
    private int defense;
    private int speed;
    private int jump;
    private int slots;

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////


    /**
     *
     */
    public PlayerStats() {
        this(0,0,0,0,0);
    }

    /**
     *
     * @param health    health
     * @param defense   defense
     * @param speed     speed
     * @param jump      jump
     * @param slots      slots
     */
    public PlayerStats(
            int health,
            int defense,
            int speed,
            int jump,
            int slots) {
        super();

        this.health = health;
        this.defense = defense;
        this.speed = speed;
        this.jump = jump;
        this.slots = slots;
    }

    public static PlayerStats getDefault() {
        return new PlayerStats(5, 0, 5, 15, 1);
    }

    /**
     *
     * @param playerStats playerStats to add
     */
    public void add(PlayerStats playerStats) {
        this.health     += playerStats.health;
        this.defense    += playerStats.defense;
        this.speed      += playerStats.speed;
        this.jump       += playerStats.jump;
        this.slots += playerStats.slots;
    }


//////////////////////////////////////////////////
//  Getter & Setter - methods
//////////////////////////////////////////////////


    public int getHealth() {
        return health;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getJump() {
        return jump;
    }

    /**
     *
     * @return returns the count of the BeltSlots
     */
    public int getSlots() {
        return this.slots;
    }

    /**
     * add one slots
     */
    public void addBeltSlot() {
        this.slots++;
    }

}
