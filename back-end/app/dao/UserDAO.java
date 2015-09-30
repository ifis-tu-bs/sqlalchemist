package dao;

import models.User;
import models.Profile;

import helper.MailSender;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class UserDAO {

  /**
   *
   * @param username  Player Username
   * @param id        User Identifier
   * @param password  User Password
   * @param role      User role
   * @return returns the user object
   */
  public static User create(
          String username,
          String id,
          String password,
          int    role) {
      User user = new User(id, password, role);
      try {
          user.save();

          Profile profile;
          if(( profile = ProfileDAO.create(username)) == null) {
              user.delete();
          } else {
              user.setProfile(profile);
              profile.setUser(user);

              user.update();
              profile.update();

              if(play.api.Play.isProd(play.api.Play.current())) {
                  MailSender.getInstance().sendVerifyEmail(user.getEmail(), user.getEmailVerifyCode());
              }
          }

          return user;
      } catch (PersistenceException ex) {
          Logger.warn("User.create PersistenceExcretion: " + ex.getMessage());
          return null;
      }
  }

  /**
   *
   */
  public static User getById(long id) {
      return User.find.byId(id);
  }

  /**
   *
   * @param email
   * @return
   */
  public static User getByEmail(String email) {
    if(email == null) {
      return null;
    }
    return User.find.where().eq("email", email).findUnique();
  }

  public static User getByUsername(String username) {
    if(username == null) {
      return null;
    }
    return ProfileDAO.getByUsername(username).getUser();
  }

  /**
   *
   * @param y_ID
   * @return
   */
  private static User getByY_ID(String y_ID) {
    if(y_ID == null) {
      return null;
    }
    return User.find.where().eq("y_ID",y_ID).findUnique();
  }

  /**
   * @return
   */
  public static List<User> getAllStudendts() {
    List<User> studentList = User.find.where().eq("isStudent", true).findList();
    if (studentList.size() == 0) {
      return null;
    }
    return studentList;
  }

  public static List<User> getAllUsers() {
    return User.find.all();
  }

}
