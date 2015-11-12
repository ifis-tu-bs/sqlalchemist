package dao;

import helper.MailSender;

import models.Profile;
import models.User;

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
              return null;
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

  public static User getById(long id) {
      return User.find.byId(id);
  }

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
    Profile userProfile = ProfileDAO.getByUsername(username);
    if (userProfile == null) {
      return null;
    }
    return userProfile.getUser();
  }

    private static User getByY_ID(String y_ID) {
        if(y_ID == null) {
            return null;
        }
        return User.find.where().eq("y_ID",y_ID).findUnique();
    }

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

    public static List<User> getAll() {
        return User.find.all();
    }

    public static User getByVerifyCode(String verifyCode) {
        return User.find.where().eq("email_verify_code", verifyCode).findUnique();
    }
}
