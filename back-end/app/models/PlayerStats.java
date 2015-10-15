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
    int health;
    int defense;
    int speed;
    int jump;
    int slot;

    public static PlayerStats defaultValues = new PlayerStats(5,0,5,15,1);

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
     * @param slot      slot
     */
    public PlayerStats(
            int health,
            int defense,
            int speed,
            int jump,
            int slot) {
        super();

        this.health = health;
        this.defense = defense;
        this.speed = speed;
        this.jump = jump;
        this.slot = slot;
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
        this.slot       += playerStats.slot;
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
    public int getSlot() {
        return this.slot;
    }

    /**
     * add one slot
     */
    public void addBeltSlot() {
        this.slot++;
    }

}
