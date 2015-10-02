package dao;

import models.Profile;

import play.Logger;

import javax.persistence.PersistenceException;

/**
 * This class containes all database getter & setters for profile objects
 */
public class ProfileDAO {
  /**
   *
   * @param username asd
   * @return asd
   */
  public static Profile create(String username) {
    Profile profile = new Profile(username);
    try {
      profile.save();

      return profile;
    } catch (PersistenceException e) {
      Logger.warn("Profile.create UsernameTaken: " + e.getMessage());
      return null;
    }
  }


  public static Profile getById(long id) {
    return Profile.find.where().eq("id", id).findUnique();
  }

  public static Profile getByUsername(String username) {
    if(username == null || username.equals("")) {
      return null;
    }
    return Profile.find.where().eq("username", username).findUnique();
  }
}
