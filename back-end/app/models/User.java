package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.jdbc.CommunicationsException;
import com.sun.mail.iap.ConnectionException;
import controllers.Application;
import Exception.EmailTakenException;
import Exception.UsernameTakenException;
import helper.HMSAccessor;
import helper.MailSender;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.naming.CommunicationException;
import javax.persistence.*;
import java.sql.SQLTimeoutException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 *
 * @version 0.5
 * @author Fabio Mazzone
 */
@Entity
@Table(name = "user")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class User extends Model {
    // unique ID
    @Id
    public long id;

    //
    @Constraints.Email
    @Column(unique = true)
    private String email;

    public static final int USER_EMAIL_VERIFY_NOT_FOUND = -1;
    public static final int USER_EMAIL_VERIFY_ALREADY_VERIFIED = -2;

    private boolean emailVerified = false;
    @Column(name = "email_verify_code", unique = true)
    private String emailVerifyCode;

    @Column(unique = true, name = "user_mnr")
    public String matNR;

    @Column(unique = true, name = "user_ynr")
    public String y_id;

    public boolean isStudent;

    private String passwordHash;

    @Column(name = "password_reset_code", unique = true)
    private String passwordResetCode;

    /**
     * 1 | User
     * 2 | Creator
     * 3 | Admin
     */
    @Column(name = "user_role")
    private int role;

    public static final int ROLE_USER = 1;
    public static final int ROLE_CREATOR = 2;
    public static final int ROLE_ADMIN = 3;

    @OneToMany(mappedBy="user")
    public List<UserSession> sessions;

    @OneToOne(mappedBy="user")
    public Profile profile;

    private Date created_at;
    private Date edited_at;

    public static final Finder<Long, User> find = new Finder<>(Long.class, User.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     *
     * @param id        email or y-ID
     * @param password  password
     * @param role      user-role
     */
    private User(
            String id,
            String password,
            int    role) {
        if( id == null || password == null || !Pattern.matches(".+@.+\\..+", id)) {
            throw new IllegalArgumentException();
        }

        this.email = id;

        this.emailVerified = false;

        long verifyNumber = (long) this.hashCode();
        long checksum = (98 - ((verifyNumber * 100) % 97)) % 97;
        this.emailVerifyCode = Long.toHexString(verifyNumber * 100 + checksum);

        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        this.role = role;

        this.created_at = new Date();
        if(play.api.Play.isProd(play.api.Play.current())) {
            User.updateStudentState(this);
        }
    }


    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        if(this.matNR != null) {
            node.put("matno",       this.matNR);
        } else {
            node.put("matno",       "");
        }
        switch (this.role) {
            case ROLE_USER:
                node.put("role",    "User");
                break;
            case ROLE_CREATOR:
                node.put("role",    "Creator");
                break;
            case ROLE_ADMIN:
                node.put("role",    "Admin");
                break;
        }

        node.put("email",       this.email);

        return node;
    }

    public ObjectNode toJsonShort () {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);

        switch (this.role) {
            case ROLE_USER:
                node.put("role",    "User");
                break;
            case ROLE_CREATOR:
                node.put("role",    "Creator");
                break;
            case ROLE_ADMIN:
                node.put("role",    "Admin");
                break;
        }

        node.put("email",       this.email);

        return node;
    }


//////////////////////////////////////////////////
//  Object Methods
//////////////////////////////////////////////////

    /**
     *
     * @param oldPassword current User Password
     * @param newPassword new User Password
     * @return  returns true if successful
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if(BCrypt.checkpw(oldPassword, this.passwordHash)) {
            this.passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            this.update();
            return true;
        }
        return false;
    }

    /**
     * Setting password (used by doResetPassword)
     * @param newPassword
     */
    private void setPassword(String newPassword) {
        this.passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        this.passwordResetCode = null;

        this.update();
    }

    /**
     *
     * @return
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @param profile User Profile
     */
    private void setProfile(Profile profile) {
        if(profile == null) {
            throw new IllegalArgumentException();
        }
        this.profile = profile;
    }

    /**
     *
     * @return returns the role of the user
     */
    public int getRole(){
        return this.role;
    }

    /**
     *
     * @return returns true if the user is a student
     */
    public boolean isStudent() {
        return this.isStudent;
    }

    /**
     * This method updates the user
     */
    @Override
    public void update() {
        this.edited_at = new Date();

        super.update();
    }

//////////////////////////////////////////////////
//  Class Methods
//////////////////////////////////////////////////

    /**
     *
     * @param username  Player Username
     * @param id        User Identifier
     * @param password  User Password
     * @return returns the user object
     */
    public static User create(
            String username,
            String id,
            String password)
            throws UsernameTakenException, EmailTakenException{

        return User.create(username, id, password, User.ROLE_USER);
    }

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
            int    role)
            throws UsernameTakenException, EmailTakenException {


        User IDCheck = find.where().eq("email", id).findUnique();
        if(IDCheck != null) {
            Logger.warn("User.create - ID TAKEN");
            throw new EmailTakenException();
        }

        Profile usernameCheck = Profile.getByUsername(username);
        if(usernameCheck != null) {
            Logger.warn("User.create - Username TAKEN");
            throw new UsernameTakenException();
        }

        User user = new User(id, password, role);
        try {
            user.save();

            Profile profile = Profile.create(username);

            user.setProfile(profile);
            profile.setUser(user);

            user.update();
            profile.update();

            if(play.api.Play.isProd(play.api.Play.current())) {
                MailSender.getInstance().sendVerifyEmail(user.email, user.emailVerifyCode);
            }

            return user;
        } catch (PersistenceException e) {
            Logger.warn("User.create PersistenceExcretion: " + e.getMessage());
            e.printStackTrace();
            user.delete();

            throw new EmailTakenException();
        }
    }

//////////////////////////////////////////////////
//  Actions Methods
//////////////////////////////////////////////////

    /**
     *
     * @param id        User Identifier
     * @param password  User password
     * @param adminTool
     * @return
     */
    public static User validate(String id, String password, boolean adminTool) {


        if(id == null || password == null) {
            return null;
        }
        /*
        if(id.charAt(0) == 'y'){
            User user = getByY_ID(id);
            // ToDo
            // LDAP Magic
        } else {
            User user = getByEmail(id);
            if (user != null && user.isStudent) {
                // ToDO WE NEED THIS?
                // LDAP Magic
            } else if (user != null &&  BCrypt.checkpw(password, user.passwordHash)) {
                if(adminTool && user.getRole() > ROLE_USER || !adminTool) {
                    return user;
                }
            }
        }
           */
        User user = dao.UserDAO.getByEmail(id);
        if (user != null &&  BCrypt.checkpw(password, user.passwordHash)) {
            if (adminTool && user.getRole() > ROLE_USER || !adminTool) {
                return user;
            }
        }
        return null;
    }

    /**
     * Send reset mail
     *
     * @return
     */
    public void sendResetPasswordMail() {


        long verifyNumber = (long) this.email.hashCode();
        long checksum = (98 - ((verifyNumber * 100) % 97)) % 97;

        this.passwordResetCode = Long.toHexString(verifyNumber * 100 + checksum);

        this.update();

        MailSender.getInstance().sendResetEmail(this.email, this.passwordResetCode);
    }

    /**
     *  Change Password for set resetCode
     * @param resetCode
     * @param newPassword
     * @return
     */
    public static boolean doResetPassword(String resetCode, String newPassword) {
        User user = find.where().eq("password_reset_code", resetCode).findUnique();

        if (user == null) {
            return false;
        }

        user.setPassword(newPassword);

        MailSender.getInstance().sendPasswordChanged(user.email);

        return true;
    }

    /**
     *  updates the isStudent flag
     * @param user
     * @return
     */
    public static boolean updateStudentState(User user) {
        HMSAccessor hms = new HMSAccessor();
        if (hms.identifyUser(user.getEmail())) {
            user.isStudent = true;

            user.y_id = hms.getResults().get("ynumber");
            user.matNR = hms.getResults().get("matnumber");
            return true;
        }
        return false;
    }

    /**
     *
     * @param emailVerifyCode verification code
     * @return returns 0 if successful
     */
    public static int verifyEmail(String emailVerifyCode) {
        User user = find.where().eq("email_verify_code", emailVerifyCode).findUnique();
        if (user == null) {
            return USER_EMAIL_VERIFY_NOT_FOUND;
        } else if(user.emailVerified) {
            return USER_EMAIL_VERIFY_ALREADY_VERIFIED;
        }
        user.emailVerified = true;
        return 0;
    }

    public void setStudent() {
        this.isStudent = true;
    }

    public void promote(int role) {
        this.role = role;
        this.save();
        MailSender.getInstance().sendPromoteMail(this);
    }
}
