package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

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
        uniqueConstraints = @UniqueConstraint(columnNames = {"type", "powerLevel"})
)
public class Potion extends Model {
    @Id
    private long id;

    @Column(name = "Potion_name", unique = true)
    private final String name;

    static final int HEALTH_POTION = 0;
    static final int SPEED_POTION = 1;
    static final int JUMP_POTION = 2;
    static final int DEFENSE_POTION = 3;

    @Column(name = "type")
    private final int type;

    static final int POWER_LEVEL_1 = 1;
    static final int POWER_LEVEL_2 = 2;
    static final int POWER_LEVEL_3 = 3;
    static final int POWER_LEVEL_4 = 4;
    static final int POWER_LEVEL_5 = 5;

    @Column(name = "powerLevel")
    private final int powerLevel;

    @Column(name = "buff_value")
    private final int buff_value;

    @Embedded
    private PlayerStats playerStats;

    private static final Finder<Long, Potion> find = new Finder<>(Long.class, Potion.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Potion constructor
     * @param name          Potion Name
     * @param type          Potion Type
     * @param powerLevel    Potion Power Level
     * @param buff_value    Potion Buff Value
     */
    private Potion(
            String name,
            int type,
            int powerLevel,
            int buff_value) {
        super();

        this.name = name;
        this.type = type;
        this.powerLevel = powerLevel;

        switch (type) {
            case HEALTH_POTION:
                this.playerStats = new PlayerStats(buff_value, 0,0,0,0);
                break;
            case SPEED_POTION:
                this.playerStats = new PlayerStats(0, buff_value, 0,0,0);
                break;
            case JUMP_POTION:
                this.playerStats = new PlayerStats(0,0, buff_value,0,0);
                break;
            case DEFENSE_POTION:
                this.playerStats = new PlayerStats(0,0,0, buff_value, 0);
                break;
            default:
                this.playerStats = new PlayerStats(0, 0,0,0,0);
                break;
        }


        this.buff_value = buff_value;
    }

    public long getId() {
        return this.id;
    }



    public int getLevel() {
        return this.powerLevel;
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
        node.put("buff_value", this.playerStats.toJson());

        return node;
    }

//////////////////////////////////////////////////
//  Create Methods
//////////////////////////////////////////////////

    /**
     * Potion create method
     * @param name          Potion Name
     * @param type          Potion Type
     * @param powerLevel    Potion Power Level
     * @param buff_value    Potion Power
     * @return              returns the Potion-Object or Null on Failure
     */
    public static Potion create(
            String name,
            int type,
            int powerLevel,
            int buff_value) {
        if (name == null) {
            return null;
        }

        Potion potion = new Potion(name, type, powerLevel, buff_value);
        try {
            potion.save();
        } catch (PersistenceException pe){
            Potion potion_comp = Potion.getByTypeAndPowerLevel(type, powerLevel);
            if(potion_comp != null && potion_comp.name.equalsIgnoreCase(name) && potion_comp.buff_value == buff_value) {
                Logger.warn("Can't create Potion(duplicate) " + potion.toJson().toString());
                return potion_comp;
            }
            Logger.error("Can't create Potion: " + potion.toJson());
            return null;
        }
        return potion;
    }

//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    public static Potion getById(long id) {
        return find.byId(id);
    }

    public static Potion getByTypeAndPowerLevel(int type, int powerLevel) {
        return Potion.find.where().eq("type", type).eq("powerLevel", powerLevel).findUnique();
    }

//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////


    public static void init(){
        Logger.info("Initialize 'Potion' data");

        Potion.create("Weak Health Potion", HEALTH_POTION, POWER_LEVEL_1, 1);
        Potion.create("Moderate Health Potion", HEALTH_POTION, POWER_LEVEL_2, 2);
        Potion.create("Great Health Potion", HEALTH_POTION, POWER_LEVEL_3, 3);
        Potion.create("Powerful Health Potion", HEALTH_POTION, POWER_LEVEL_4, 4);
        Potion.create("Insane Health Potion", HEALTH_POTION, POWER_LEVEL_5, 5);

        //Speed * 2
        Potion.create("Weak Speed Potion", SPEED_POTION, POWER_LEVEL_1, 2);
        Potion.create("Moderate Speed Potion", SPEED_POTION, POWER_LEVEL_2, 4);
        Potion.create("Great Speed Potion", SPEED_POTION, POWER_LEVEL_3, 6);
        Potion.create("Powerful Speed Potion", SPEED_POTION, POWER_LEVEL_4, 8);
        Potion.create("Insane Speed Potion", SPEED_POTION, POWER_LEVEL_5, 10);

        //Jump * 3
        Potion.create("Weak Jump Potion", JUMP_POTION, POWER_LEVEL_1, 3);
        Potion.create("Moderate Jump Potion", JUMP_POTION, POWER_LEVEL_2, 6);
        Potion.create("Great Jump Potion", JUMP_POTION, POWER_LEVEL_3, 9);
        Potion.create("Powerful Jump Potion", JUMP_POTION, POWER_LEVEL_4, 12);
        Potion.create("Insane Jump Potion", JUMP_POTION, POWER_LEVEL_5, 15);

        //Def * 2
        Potion.create("Weak Defense Potion", DEFENSE_POTION, POWER_LEVEL_1, 2);
        Potion.create("Moderate Defense Potion", DEFENSE_POTION, POWER_LEVEL_2, 4);
        Potion.create("Great Defense Potion", DEFENSE_POTION, POWER_LEVEL_3, 6);
        Potion.create("Powerful Defense Potion", DEFENSE_POTION, POWER_LEVEL_4, 8);
        Potion.create("Insane Defense Potion", DEFENSE_POTION, POWER_LEVEL_5, 10);

        Logger.info("Done initializing");
    }

    public static List<Potion> getAll() {
        List<Potion> potionList = find.all();

        if(potionList == null) {
            Logger.warn("Potion.getAll - no potions in database !?");
            return null;
        }
        return potionList;
    }
}
