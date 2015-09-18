package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;

/**
 * Definition of scrolls in the database
 * @author gabrielahlers
 * @version 0.5
 */
@Entity
@Table (name = "scroll")
public class Scroll extends  Model {
    @Id
    private long id;

    @Column(name = "posId", unique = true)
    private final int posId;

    @Column(name = "Scrollname", unique = true)
    private final String name;

    public  static final int TYPE_RECIPE = -1;
    public static final int TYPE_SCROLL_HEALTH = 0;
    private static final int TYPE_SCROLL_SPEED = 1;
    private static final int TYPE_SCROLL_JUMP = 2;
    public static final int TYPE_SCROLL_DEFENSE = 3;

    @Column(name = "type")
    private final int type;

    @OneToOne
    @Column(name = "potion", unique = true)
    private Potion potion;

    @Embedded
    private PlayerStats playerstats;

    private static final Finder<Long, Scroll> find = new Finder<>(Long.class, Scroll.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Constructor for the Scroll
     * @param posId Position ID
     * @param name Scroll_name
     * @param type Scroll_type
     * @param potion Scroll_potion if the scroll redeems a potion
     */
    private Scroll(
            int posId,
            String name,
            int type,
            Potion potion) {
        super();

        this.posId = posId;
        this.name = name;


        switch (type) {
            case TYPE_SCROLL_HEALTH:
                this.playerstats = new PlayerStats(1,0,0,0,0);
                break;
            case TYPE_SCROLL_DEFENSE:
                this.playerstats = new PlayerStats(0,1,0,0,0);
                break;
            case TYPE_SCROLL_SPEED:
                this.playerstats = new PlayerStats(0,0,1,0,0);
                break;
            case TYPE_SCROLL_JUMP:
                this.playerstats = new PlayerStats(0,0,0,1,0);
                break;
            case TYPE_RECIPE:
                this.playerstats = new PlayerStats();
        }

        if(potion != null) {
            this.potion = potion;
            this.type = TYPE_RECIPE;
        } else {
            this.type = type;


        }
    }


    public int getType() {
        return type;
    }

    public boolean isRecipe() {
        return this.type == TYPE_RECIPE;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public PlayerStats getPlayerStats() {
        return this.playerstats;
    }

    public long getPosId() {
        return this.posId;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     * Json method
     * @return returns the Scroll-object as a Json model
     */
    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id", this.id);
        node.put("posId", this.posId);
        node.put("name", this.name);
        node.put("type", this.type);
        if(this.potion != null) {
            node.put("potion", this.potion.toJson());
        }

        return node;
    }

//////////////////////////////////////////////////
//  Create Methods
//////////////////////////////////////////////////

    /**
     * Scroll create method
     * @param posId Position Id in the maps
     * @param name name of the scroll
     * @param type scroll type
     * @param potion reference to the potion the scroll redeems
     * @return Scroll, if it was created
     */
    public static Scroll create(
            int posId,
            String name,
            int type,
            Potion potion) {
        if (name == null) {
            Logger.warn("Scroll.create - name is null!");
            return null;
        }

        Scroll scroll = new Scroll(posId, name, type, potion);

        try {
            scroll.save();
        } catch (PersistenceException pe) {
            Logger.warn("Scroll.create - catches PersistenceException");
            Scroll scroll_comp = Scroll.getByPosId(posId);
            if (scroll_comp != null
                    && scroll_comp.posId == posId
                    && scroll_comp.name.equalsIgnoreCase(name)) {
                Logger.warn("Can't create scroll(duplicate): " + scroll_comp.toJson().toString());
                return scroll_comp;
            }
            Logger.error("Can't create Scroll: " + scroll.toJson());
            return null;
        }
        return scroll;
    }




//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    /**
     * Get a scroll by the id
     * @param id generic id in the database
     * @return Scroll with the id
     */
    public static Scroll getById(long id) {
        return find.byId(id);
    }

    /**
     * Get a scroll by the position id in the game
     * @param posId position id on the map
     * @return Scroll with the posId
     */
    public static Scroll getByPosId(int posId) {
        return Scroll.find.where().eq("posId", posId).findUnique();
    }

    /**
     * Get a scroll by the potion it redeems
     * @param potion Potion the scroll redeems
     * @return Scroll that redeems the potion
     */
    public static Scroll getByPotion(Potion potion) {
        return Scroll.find.where().eq("potion", potion).findUnique();
    }

    //ToDo fix in create
    public static Scroll getByType(int type) {
        return Scroll.find.where().eq("type", type).findUnique();
    }

//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////


    /**
     * Method to initialize the scroll data
     */
    public static void init() {
        Logger.info("Initializing 'Scroll - Recipe' data");

        Scroll.create(0, "Recipe for the Weak Health Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.HEALTH_POTION, Potion.POWER_LEVEL_1));
        Scroll.create(1, "Recipe for the Moderate Health Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.HEALTH_POTION, Potion.POWER_LEVEL_2));
        Scroll.create(2, "Recipe for the Great Health Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.HEALTH_POTION, Potion.POWER_LEVEL_3));
        Scroll.create(3, "Recipe for the Powerful Health Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.HEALTH_POTION, Potion.POWER_LEVEL_4));
        Scroll.create(4, "Recipe for the Insane Health Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.HEALTH_POTION, Potion.POWER_LEVEL_5));

        Scroll.create(5, "Recipe for the Weak Speed Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.SPEED_POTION, Potion.POWER_LEVEL_1));
        Scroll.create(6, "Recipe for the Moderate Speed Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.SPEED_POTION, Potion.POWER_LEVEL_2));
        Scroll.create(7, "Recipe for the Great Speed Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.SPEED_POTION, Potion.POWER_LEVEL_3));
        Scroll.create(8 ,"Recipe for the Powerful Speed Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.SPEED_POTION, Potion.POWER_LEVEL_4));
        Scroll.create(9, "Recipe for the Insane Speed Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.SPEED_POTION, Potion.POWER_LEVEL_5));

        Scroll.create(10, "Recipe for the Weak Jump Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.JUMP_POTION, Potion.POWER_LEVEL_1));
        Scroll.create(11, "Recipe for the Moderate Jump Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.JUMP_POTION, Potion.POWER_LEVEL_2));
        Scroll.create(12, "Recipe for the Great Jump Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.JUMP_POTION, Potion.POWER_LEVEL_3));
        Scroll.create(13, "Recipe for the Powerful Jump Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.JUMP_POTION, Potion.POWER_LEVEL_4));
        Scroll.create(14, "Recipe for the Insane Jump Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.JUMP_POTION, Potion.POWER_LEVEL_5));

        Scroll.create(15, "Recipe for the Weak Defense Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.DEFENSE_POTION, Potion.POWER_LEVEL_1));
        Scroll.create(16, "Recipe for the Moderate Defense Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.DEFENSE_POTION, Potion.POWER_LEVEL_2));
        Scroll.create(17, "Recipe for the Great Defense Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.DEFENSE_POTION, Potion.POWER_LEVEL_3));
        Scroll.create(18, "Recipe for the Powerful Defense Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.DEFENSE_POTION, Potion.POWER_LEVEL_4));
        Scroll.create(19, "Recipe for the Insane Defense Potion", TYPE_RECIPE,
                Potion.getByTypeAndPowerLevel(Potion.DEFENSE_POTION, Potion.POWER_LEVEL_5));

        Logger.info("Initializing 'Scroll - Enchantment' data");

        Scroll.create(20, "Endurance of a hog", TYPE_SCROLL_HEALTH, null);
        Scroll.create(21, "Boyce Codd training", TYPE_SCROLL_HEALTH, null);
        Scroll.create(22, "Iron will", TYPE_SCROLL_HEALTH, null);
        Scroll.create(23, "Increased strength", TYPE_SCROLL_HEALTH, null);
        Scroll.create(24, "Heart of a lion", TYPE_SCROLL_HEALTH, null);
        Scroll.create(25, "Healing factor", TYPE_SCROLL_HEALTH, null);
        Scroll.create(26, "Enpowered spirit", TYPE_SCROLL_HEALTH, null);
        Scroll.create(27, "Stamina of a bull", TYPE_SCROLL_HEALTH, null);
        Scroll.create(28, "A sip of unicornblood", TYPE_SCROLL_HEALTH, null);
        Scroll.create(29, "Blood of a giant", TYPE_SCROLL_HEALTH, null);

        Scroll.create(30, "Tracking boots", TYPE_SCROLL_SPEED, null);
        Scroll.create(31, "Light armor", TYPE_SCROLL_SPEED, null);
        Scroll.create(32, "A hot pot of coffee", TYPE_SCROLL_SPEED, null);
        Scroll.create(33, "Squirrel entchantment", TYPE_SCROLL_SPEED, null);
        Scroll.create(34, "Painting yellow arrows on the ground", TYPE_SCROLL_SPEED, null);
        Scroll.create(35, "Another pointy hat", TYPE_SCROLL_SPEED, null);
        Scroll.create(36, "Rocket boots", TYPE_SCROLL_SPEED, null);
        Scroll.create(37, "Paint a lightning on your chest", TYPE_SCROLL_SPEED, null);
        Scroll.create(38, "Chaos Diamond", TYPE_SCROLL_SPEED, null);
        Scroll.create(39, "Speed, yes the drug.", TYPE_SCROLL_SPEED, null);

        Scroll.create(40, "Jumping springs", TYPE_SCROLL_JUMP, null);
        Scroll.create(41, "The Golden Snitch", TYPE_SCROLL_JUMP, null);
        Scroll.create(42, "Levitation enchantment", TYPE_SCROLL_JUMP, null);
        Scroll.create(43, "Flying umbrella", TYPE_SCROLL_JUMP, null);
        Scroll.create(44, "The good old swish and flick", TYPE_SCROLL_JUMP, null);
        Scroll.create(45, "Weed to get high", TYPE_SCROLL_JUMP, null);
        Scroll.create(46, "A pointy hat for great aerodynamics", TYPE_SCROLL_JUMP, null);
        Scroll.create(47, "The broom entchantment for humans", TYPE_SCROLL_JUMP, null);
        Scroll.create(48, "Winged boots", TYPE_SCROLL_JUMP, null);
        Scroll.create(49, "Ravenclaws diadem", TYPE_SCROLL_JUMP, null);

        Scroll.create(50, "Evil repelling coat", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(51, "Aura of defense", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(52, "Hardened leather boots", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(53, "Fancy pants", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(54, "The shell of a svnTortoise", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(55, "Titanium rope", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(56, "Impenetrable skin", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(57, "Becoming a fullmetal alchemist", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(58, "Scales of a dragon", TYPE_SCROLL_DEFENSE, null);
        Scroll.create(59, "Impenetrable armor of doom,epicness, invicibility and awesomeness", TYPE_SCROLL_DEFENSE, null);

        Logger.info("Done initializing");
    }


}
