package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

/**
 * Class for the user-inventory
 * @author gabrielahlers
 * @version 1.0
 */
@Entity
@Table (name = "Inventory")
public class Inventory extends Model{
    @Id
    private long id;

    @ManyToOne
    @Column(name = "profile")
    private final Profile profile;

    @ManyToOne
    @Column(name = "potion")
    private final Potion potion;


    @Column(name = "beltSlot")
    private int beltSlot;

    private static final Finder<Long, Inventory> find = new Finder<>(Long.class, Inventory.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Constructor of the inventory
     * @param profile Connection to the user profile
     * @param potion Connection to the potion in the slot
     */
    public Inventory(
            Profile profile,
            Potion potion){
        super();

        this.profile    = profile;
        this.potion     = potion;
        this.beltSlot   = 0;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     * Json method to return the the entire inventory of a player
     * @param profile Reference to the profile of the player
     * @return returns the inventory as an ArrayNode
     */
    public static ObjectNode getJson_Inventory(Profile profile){
        List<Potion> potionList = Potion.getAll();
        ObjectNode json = Json.newObject();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for(Potion potion : potionList) {
            ObjectNode objectNode = Json.newObject();
            List<Inventory> inventoryList = getInventoryList(profile, potion);
            int count = 0;
            if(inventoryList != null) {
                count = inventoryList.size();
            }

            objectNode.put("potion",    potion.toJson());
            objectNode.put("count",     count);

            Scroll scroll = Scroll.getByPotion(potion);
            if(ScrollCollection.isActive(profile, scroll)) {
                objectNode.put("scroll", scroll.toJson());
            } else {
                objectNode.put("scroll", "empty");
            }

            arrayNode.add(objectNode);
        }

        json.put("potions", arrayNode);
        return json;
    }

    /**
     * Json object with the content of a players belt
     * @param profile Reference to the profile of the player
     * @return Node with the beltsize and items in the beltSlots of the player
     */
    public static ObjectNode getJson_Belt(Profile profile){
        ObjectNode node = Json.newObject();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        node.put("slots", profile.getPlayerStats().getSlot());

        for (int i = 1; i <= profile.getPlayerStats().getSlot(); i++){
            ObjectNode json = Json.newObject();
            Inventory inv = getBeltSlot(profile, i);
            json.put("slot", i);
            if (inv == null) {
                json.put("potion", "empty");
            } else {
                json.put("potion", inv.potion.toJson());
            }
            arrayNode.add(json);
        }
        node.put("slots", arrayNode);
        return node;
    }


    public static void updateBeltSlot(
            Profile profile,
            Potion  potion,
            int     slot) {

        Inventory inventory = Inventory.getInventoryList(profile, potion).get(0);
        if (inventory != null) {
            inventory.delete();
            inventory = new Inventory(profile, potion);
            inventory.setBeltSlot(slot);

            inventory.save();
        }
    }

    public void setBeltSlot(int beltSlot) {
        this.beltSlot = beltSlot;
    }

//////////////////////////////////////////////////
//  Create Methods
//////////////////////////////////////////////////
    public static Inventory create(
            Profile profile,
            Potion potion){

        if(profile == null || potion == null) {
            Logger.warn("Inventory.create - profile or potion is null");
            return null;
        }

        Inventory inv = new Inventory(profile, potion);

        try {
            inv.save();
        } catch (PersistenceException pe) {
            Logger.error("Inventory.create - Couldn't create Inventory: " + inv.profile.getId() + " + " + inv.potion.getId());
            Logger.warn("                  - " + pe.getLocalizedMessage());
        }

        return inv;
    }

//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    /**
     * Get method to the get the inventory of one profile
     * @param profile Reference to the profile
     * @return Get result
     */
    public static List<Inventory> getInventoryList(Profile profile, Potion potion){
        return Inventory.find.where().eq("profile", profile).eq("potion", potion).eq("beltSlot", 0).findList();
    }

    /**
     * Get method to get a certain beltSlot
     * @param profile Reference to the profile
     * @return Get result
     */
    public static Inventory getBeltSlot(Profile profile, int beltSlot){
        List<Inventory> inventory = find.where().eq("profile", profile).eq("beltSlot", beltSlot).findList();
        if(inventory.size() > 0) {
            return inventory.get(0);
        }
        return null;
    }

//////////////////////////////////////////////////
//  Update Function
//////////////////////////////////////////////////

    public static void clearBelt(Profile profile){

        List<Inventory> inv_list = Inventory.find.where().eq("profile", profile).isNotNull("beltSlot").findList();
        for (Inventory inv : inv_list){
            Potion potion = inv.potion;

            inv.delete();

            Inventory.create(profile, potion);
        }
    }

    public static void potionUsed(Profile profile, JsonNode node){

        int p_id = node.get("potion").asInt();
        int slot = node.get("slot").asInt();

        Inventory usedPotion = Inventory.find.where().eq("profile", profile).eq("potion", p_id).eq("beltSlot", slot).findList().get(0);

        usedPotion.delete();

    }

    public static void reset(Profile profile) {
        List<Inventory> inventoryList = find.where().eq("profile", profile).findList();

        for(Inventory inventory : inventoryList) {
            inventory.delete();
        }
    }
}



