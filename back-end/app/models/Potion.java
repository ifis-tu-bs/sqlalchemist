package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.avaje.ebean.Model;
import play.libs.Json;
import view.PlayerStatsView;

import javax.persistence.*;

/**
 *  Mapping-Class for Potions
 *
 *  @version 1.0
 *  @author fabiomazzone
 *  @author gabrielahlers
 */
@Entity
@Table(
        name = "potion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"type", "power_level"})
)
public class Potion extends Model {
    @Id
    private long id;

    @Column(unique = true)
    private final String name;

    public static final int HEALTH_POTION = 0;
    public static final int SPEED_POTION = 1;
    public static final int JUMP_POTION = 2;
    public static final int DEFENSE_POTION = 3;

    private final int type;

    public static final int POWER_LEVEL_1 = 1;
    public static final int POWER_LEVEL_2 = 2;
    public static final int POWER_LEVEL_3 = 3;
    public static final int POWER_LEVEL_4 = 4;
    public static final int POWER_LEVEL_5 = 5;

    private final int powerLevel;

    private final int buffValue;

    @Embedded
    private final PlayerStats playerStats;

    public static final Finder<Long, Potion> find = new Finder<>(Potion.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Potion constructor
     * @param name          Potion Name
     * @param type          Potion Type
     * @param powerLevel    Potion Power Level
     * @param buffValue    Potion Buff Value
     */
    public Potion(
            String name,
            int type,
            int powerLevel,
            int buffValue) {
        super();

        this.name = name;
        this.type = type;
        this.powerLevel = powerLevel;

        switch (type) {
            case HEALTH_POTION:
                this.playerStats = new PlayerStats(buffValue, 0,0,0,0);
                break;
            case SPEED_POTION:
                this.playerStats = new PlayerStats(0, buffValue, 0,0,0);
                break;
            case JUMP_POTION:
                this.playerStats = new PlayerStats(0,0, buffValue,0,0);
                break;
            case DEFENSE_POTION:
                this.playerStats = new PlayerStats(0,0,0, buffValue, 0);
                break;
            default:
                this.playerStats = new PlayerStats(0, 0,0,0,0);
                break;
        }


        this.buffValue = buffValue;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
      return this.name;
    }

    public int getPowerLevel() {
        return this.powerLevel;
    }

    public int getBuffValue() {
      return this.buffValue;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     * Json method
     * @return returns the Potion-Object as a Json model
     */
    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id", this.id);
        node.put("name", this.name);
        node.put("type", this.type);
        node.put("powerLevel", this.powerLevel);
        node.set("buff_value", PlayerStatsView.toJson(this.playerStats));

        return node;
    }
}
