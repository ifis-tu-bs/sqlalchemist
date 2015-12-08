package dao;

import com.avaje.ebean.Model;
import models.Avatar;
import models.PlayerStats;

import play.Logger;
import play.libs.Json;

import javax.persistence.PersistenceException;
import java.util.List;

public class AvatarDAO {
    private static final Model.Finder<Long, Avatar> find = new Model.Finder<>(Avatar.class);
    //////////////////////////////////////////////////
    //  Create Method
    //////////////////////////////////////////////////
  /**
   * This is a create method for an avatar-object
   * @param name                  name of the avatar
   * @param desc                  description of the avatar
   * @param avatarFilename        file name of the avatar files
   * @param isTeam                is the avatar a tag team
   * @param playerStats           avatar playerStats
   */
    public static Avatar create(
            String name,
            String desc,
            String avatarFilename,
            boolean isTeam,
            PlayerStats playerStats) {

        Avatar avatar = new Avatar(
                name,
                desc,
                avatarFilename,
                isTeam,
                playerStats );
        try {

            avatar.save();
        } catch (PersistenceException pe) {
            Logger.warn(pe.getMessage());
            Avatar avatar_res = find.where().eq("avatar_name", name).findUnique();
            if(avatar_res != null && avatar_res.getName().equalsIgnoreCase(name)) {
                Logger.warn("Can't create Avatar(duplicate) " + Json.toJson(avatar).toString());
                return avatar_res;
            }
            Logger.error("Can't create Avatar: " + Json.toJson(avatar));
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
    return find.byId(id);
  }

  /**
   * Get an avatar object by its name
   *
   * @param name  name of the Avatar
   * @return      returns the Avatar that matches to the given name
   */
  public static Avatar getByName(String name) {
    return find.where().eq("name", name).findUnique();
  }

    public static List<Avatar> getAll() {
        return find.all();
    }
}
