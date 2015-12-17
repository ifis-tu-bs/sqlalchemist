package dao;

import com.avaje.ebean.Model;
import forms.SignUp;
import helper.MailSender;

import models.Session;
import models.User;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class UserDAO {
    public static Model.Finder<Long, User> find = new Model.Finder<>(User.class);

    /**
     *
     * @param signUp  signUp Data
     * @return returns the user object
     */
    public static User create(SignUp signUp) {
        User user;
        user = new User(
                signUp.getEmail(),
                signUp.getUsername(),
                signUp.getPassword(),
                RoleDAO.getUser()
        );

        try {
            user.save();

            if(play.api.Play.isProd(play.api.Play.current())) {
                MailSender.getInstance().sendVerifyEmail(user.getEmail(), user.getEmailVerifyCode());
            }

            return user;
        } catch (PersistenceException ex) {
            Logger.warn("User.create PersistenceExcretion: " + ex.getMessage());
            return null;
        }
    }

    public static User getById(long id) {
        return find.byId(id);
    }

    public static User getByEmail(String email) {
        if(email == null) {
            return null;
        }
        return find.where().eq("email", email).findUnique();
    }

    public static User getByUsername(String username) {
        if(username == null) {
            return null;
        }
        return find.where().eq("username", username).findUnique();
    }


    public static User getBySession(String sessionID) {
        Session session = SessionDAO.getById(sessionID);
        if(session != null && session.getOwner() != null) {
            return session.getOwner();
        }
        return null;
    }

    private static User getByY_ID(String y_ID) {
        if(y_ID == null) {
            return null;
        }
        return find.where().eq("y_id",y_ID).findUnique();
    }

    public static List<User> getAllStudents() {
        List<User> studentList = find.where().or(find.getExpressionFactory().isNotNull("y_id"), find.getExpressionFactory().isNotNull("mat_nr")).findList();
        if (studentList.size() == 0) {
            return null;
        }
        return studentList;
    }

    public static List<User> getAll() {
        return find.all();
    }

    public static User getByVerifyCode(String verifyCode) {
        return find.where().eq("email_verify_code", verifyCode).findUnique();
    }
}
