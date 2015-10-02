package dao;

import models.Avatar;
import models.PlayerStats;

import play.Logger;

import javax.persistence.PersistenceException;

public class AvatarDAO {

  //////////////////////////////////////////////////
  //  Create Method
  //////////////////////////////////////////////////
  /**
   * This is a create method for an avatar-object
   *
   * @param name                  name of the avatar
   * @param desc                  description of the avatar
   * @param avatarFilename        file name of the avatar files
   * @param soundURL              sound url
   * @param isTeam                is the avatar a tag team
   * @param playerStats           avatar playerStats
   */
  public static Avatar create(
      String  name,
      String  desc,
      String  avatarFilename,
      String  soundURL,
      boolean isTeam,
      PlayerStats playerStats) {

    Avatar avatar = new Avatar(
        name,
        desc,
        avatarFilename,
        soundURL,
        isTeam,
        playerStats );

    try {
      avatar.save();
    } catch (PersistenceException pe) {
      Avatar avatar_res = Avatar.find.where().eq("name", name).findUnique();
      if(avatar_res != null && avatar_res.getName().equalsIgnoreCase(name)) {
        Logger.warn("Can't create Avatar(duplicate) " + avatar.toJson().toString());
        return avatar_res;
      }
      Logger.error("Can't create Avatar: " + avatar.toJson());
      return null;
    }
    return avatar;
  }

  //////////////////////////////////////////////////
  //  Object Getter Methods
  //////////////////////////////////////////////////

  /**
   * Get an avatar by its id
   * @param       id id of the avatar
   * @return       Avatar with the id
  */
  public static Avatar getById(long id) {
    return Avatar.find.byId(id);
  }

  /**
   * Get an avatar object by its name
   *
   * @param name  name of the Avatar
   * @return      returns the Avatar that matches to the given name
   */
  public static Avatar getByName(String name) {
    return Avatar.find.where().eq("name", name).findUnique();
  }
}
