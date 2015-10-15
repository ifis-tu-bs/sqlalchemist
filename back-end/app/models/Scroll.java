package models;

import com.fasterxml.jackson.databind.node.ObjectNode;

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

    public static final int TYPE_RECIPE = -1;
    public static final int TYPE_SCROLL_HEALTH = 0;
    public static final int TYPE_SCROLL_SPEED = 1;
    public static final int TYPE_SCROLL_JUMP = 2;
    public static final int TYPE_SCROLL_DEFENSE = 3;

    @Column(name = "type")
    private final int type;

    @OneToOne
    @Column(name = "potion", unique = true)
    private Potion potion;

    @Embedded
    private PlayerStats playerstats;

    public static final Finder<Long, Scroll> find = new Finder<>(Long.class, Scroll.class);


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
    public Scroll(
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

    public long getPosId() {
        return this.posId;
    }

    public String getName() {
      return this.name;
    }

    public int getType() {
        return type;
    }


    public Potion getPotion() {
      return this.potion;
    }

    public PlayerStats getPlayerStats() {
        return this.playerstats;
    }

    public boolean isRecipe() {
        return this.type == TYPE_RECIPE;
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
}
