package dao;

import models.Potion;

import play.Logger;

import java.util.List;
import javax.persistence.PersistenceException;

public class PotionDAO {
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
      int buffValue) {
    if (name == null) {
      return null;
    }

    Potion potion = new Potion(name, type, powerLevel, buffValue);
    try {
      potion.save();
    } catch (PersistenceException pe){
      Potion potion_comp = getByTypeAndPowerLevel(type, powerLevel);
      if(potion_comp != null && potion_comp.getName().equalsIgnoreCase(name) && potion_comp.getBuffValue() == buffValue) {
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
    return Potion.find.byId(id);
  }

  public static Potion getByTypeAndPowerLevel(int type, int powerLevel) {
    return Potion.find.where().eq("type", type).eq("powerLevel", powerLevel).findUnique();
  }

  public static List<Potion> getAll() {
    List<Potion> potionList = Potion.find.all();
    if(potionList == null) {
      Logger.warn("Potion.getAll - no potions in database !?");
      return null;
    }
    return potionList;
  }
}
