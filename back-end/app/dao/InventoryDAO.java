package dao;

import com.avaje.ebean.Model;
import models.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.Logger;

import javax.persistence.PersistenceException;

import java.util.List;

public class InventoryDAO {
    private static final Model.Finder<Long, Inventory> find = new Model.Finder<>(Inventory.class);

  //////////////////////////////////////////////////
  //  Create Methods
  //////////////////////////////////////////////////
    public static Inventory create(
            User user,
            Potion potion){

        if(user == null || potion == null) {
            Logger.warn("Inventory.create - profile or potion is null");
            return null;
        }

        Inventory inv = new Inventory(user, potion);

        try {
            inv.save();
        } catch (PersistenceException pe) {
            Logger.error("Inventory.create - Couldn't create Inventory: " + inv.getUser().getId() + " + " + inv.getPotion().getId());
            Logger.warn("                  - " + pe.getLocalizedMessage());
        }

        return inv;
    }


  //////////////////////////////////////////////////
  //  Json Method
  //////////////////////////////////////////////////

      /**
       * Json method to return the the entire inventory of a player
       * @param user Reference to the profile of the player
       * @return returns the inventory as an ArrayNode
       */
      public static ObjectNode getJson_Inventory(User user){
          List<Potion> potionList = PotionDAO.getAll();
          ObjectNode json = Json.newObject();
          ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

          for(Potion potion : potionList) {
              ObjectNode objectNode = Json.newObject();
              List<Inventory> inventoryList = getInventoryList(user, potion);
              int count = 0;
              if(inventoryList != null) {
                  count = inventoryList.size();
              }

              objectNode.set("potion",    potion.toJson());
              objectNode.put("count",     count);

              Scroll scroll = ScrollDAO.getByPotion(potion);
              if(ScrollCollectionDAO.isActive(user, scroll)) {
                  objectNode.set("scroll", scroll.toJson());
              } else {
                  objectNode.put("scroll", "empty");
              }

              arrayNode.add(objectNode);
          }

          json.set("potions", arrayNode);
          return json;
      }

      /**
       * Json object with the content of a players belt
       * @param user Reference to the profile of the player
       * @return Node with the beltsize and items in the beltSlots of the player
       */
      public static ObjectNode getJson_Belt(User user){
          ObjectNode node = Json.newObject();
          ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

          node.put("slots", user.getPlayerStats().getSlot());

          for (int i = 1; i <= user.getPlayerStats().getSlot(); i++){
              ObjectNode json = Json.newObject();
              Inventory inv = getBeltSlot(user, i);
              json.put("slot", i);
              if (inv == null) {
                  json.put("potion", "empty");
              } else {
                  json.set("potion", inv.getPotion().toJson());
              }
              arrayNode.add(json);
          }
          node.set("slots", arrayNode);
          return node;
      }

    public static void updateBeltSlot(
            User      user,
            Potion    potion,
            int       slot) {

        Inventory inventory = getInventoryList(user, potion).get(0);
        if (inventory != null) {
            inventory.delete();
            inventory = new Inventory(user, potion);
            inventory.setBeltSlot(slot);

            inventory.save();
        }
    }

  //////////////////////////////////////////////////
  //  Object Getter Methods
  //////////////////////////////////////////////////

      /**
       * Get method to the get the inventory of one profile
       * @param user Reference to the profile
       * @return Get result
       */
      public static List<Inventory> getInventoryList(User user, Potion potion){
          return find.where().eq("user", user).eq("potion", potion).eq("beltSlot", 0).findList();
      }

      /**
       * Get method to get a certain beltSlot
       * @param user Reference to the profile
       * @return Get result
       */
      public static Inventory getBeltSlot(User user, int beltSlot){
          List<Inventory> inventory = find.where().eq("user", user).eq("beltSlot", beltSlot).findList();
          if(inventory.size() > 0) {
              return inventory.get(0);
          }
          return null;
      }

  //////////////////////////////////////////////////
  //  Update Function
  //////////////////////////////////////////////////

      public static void clearBelt(User user){

          List<Inventory> inv_list = find.where().eq("user", user).isNotNull("beltSlot").findList();
          for (Inventory inv : inv_list){
              Potion potion = inv.getPotion();

              inv.delete();

              InventoryDAO.create(user, potion);
          }
      }

      public static void potionUsed(User user, JsonNode node){

          int p_id = node.get("potion").asInt();
          int slot = node.get("slot").asInt();

          Inventory usedPotion = find.where().eq("user", user).eq("potion", p_id).eq("beltSlot", slot).findList().get(0);

          usedPotion.delete();

      }

      public static void reset(User user) {
          List<Inventory> inventoryList = find.where().eq("user", user).findList();

          inventoryList.forEach(models.Inventory::delete);
      }
}
