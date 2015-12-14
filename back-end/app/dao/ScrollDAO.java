package dao;

import models.Potion;
import models.Scroll;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class ScrollDAO {
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
              Scroll scroll_comp = getById(posId);
              if (scroll_comp != null
                      && scroll_comp.getPosId() == posId
                      && scroll_comp.getName().equalsIgnoreCase(name)) {
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
          return Scroll.find.byId(id);
      }

      /**
       * Get a scroll by the position id in the game
       * @param id position id on the map
       * @return Scroll with the posId
       */
      public static Scroll getByPosId(int id) {
          return Scroll.find.where().eq("pos_id", id).findUnique();
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

    public static List<Scroll> getAll() {
        return Scroll.find.all();
    }
}
