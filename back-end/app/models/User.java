package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helper.HMSAccessor;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.libs.Json;
import helper.MailSender;

import javax.persistence.*;
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
    public Long id;

    //
    @Constraints.Email
    @Column(unique = true)
    private String email;

    public static final int USER_EMAIL_VERIFY_NOT_FOUND = -1;
    public static final int USER_EMAIL_VERIFY_ALREADY_VERIFIED = -2;

    private boolean emailVerified = false;
    @Column(unique = true)
    private String emailVerifyCode;

    @Column(unique = true)
    public String matNR;

    @Column(unique = true)
    public String y_id;

    public boolean isStudent;

    private String password;

    /**
     * 1 | User
     * 2 | Creator
     * 3 | Admin
     */
    private int role;

    public static final int ROLE_USER = 1;
    public static final int ROLE_CREATOR = 2;
    public static final int ROLE_ADMIN = 3;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    public List<UserSession> sessions;

    @OneToOne(mappedBy="user", cascade = CascadeType.ALL)
    public Profile profile;

    private Date created_at;
    private Date edited_at;

    public static final Finder<Long, User> find = new Finder<>(User.class);
    private boolean isActive = true;

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     *
     * @param id        email or y-ID
     * @param password  password
     * @param role      user-role
     */
    public User(
            String id,
            String password,
            int    role) {
        if( id == null || password == null || !Pattern.matches(".+@.+\\..+", id)) {
            throw new IllegalArgumentException("Email is invalid");
        }

        this.email = id;

        this.emailVerified = false;

        long verifyNumber = (long) this.hashCode();
        long checksum = (98 - ((verifyNumber * 100) % 97)) % 97;

        this.emailVerifyCode = Long.toHexString(verifyNumber * 100 + checksum);

        this.setPassword(password);

        this.role = role;

        this.created_at = new Date();
        if(play.api.Play.isProd(play.api.Play.current())) {
            User.updateStudentState(this);
        }
    }


    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        node.put("username",    this.getProfile().getUsername());
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
        node.put("createdAt",   String.valueOf(this.created_at));

        return node;
    }

    public ObjectNode toJsonShort () {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);

        node.put("role",        this.getUserRoleString());

        node.put("email",       this.email);

        return node;
    }

    private String getUserRoleString() {
        String userRole = "";
        switch (this.role) {
            case ROLE_USER:
                userRole = "User";
                break;
            case ROLE_CREATOR:
                userRole = "Creator";
                break;
            case ROLE_ADMIN:
                userRole = "Admin";
                break;
        }

        if(this.isStudent)
            userRole += " & Student";

        return userRole;
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
        if(BCrypt.checkpw(oldPassword, this.password)) {
            this.setPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Setting password (used by doResetPassword)
     * @param newPassword    asd
     */
    public void setPassword(String newPassword) {
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    /**
     *
     * @return asd
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     *
     * @return asd
     */
    public String getEmail() {
        return this.email;
    }

    public String getEmailVerifyCode() {
        return this.emailVerifyCode;
    }

    /**
     *
     * @param profile User Profile
     */
    public void setProfile(Profile profile) {
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
//  Actions Methods
//////////////////////////////////////////////////

    /**
     *
     * @param id        User Identifier
     * @param password  User password
     * @param adminTool asd
     * @return  asd
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
            } else if (user != null &&  BCrypt.checkpw(password, user.password)) {
                if(adminTool && user.getRole() > ROLE_USER || !adminTool) {
                    return user;
                }
            }
        }
           */
        User user = dao.UserDAO.getByEmail(id);
        if (user != null &&  BCrypt.checkpw(password, user.password)) {
            if (adminTool && user.getRole() > ROLE_USER || !adminTool) {
                return user;
            }
        }
        return null;
    }

    /**
     *  updates the isStudent flag
     * @param user asd
     * @return asd
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

    public void setStudent() {
        this.isStudent = true;
    }

    public void promote(int role) {
        this.role = role;
        this.save();
        MailSender.getInstance().sendPromoteMail(this);
    }

    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified() {
        this.emailVerified = true;
        this.emailVerifyCode = null;
    }

    public boolean isAdmin() {
        return this.role == User.ROLE_ADMIN;
    }

    public void disable() {
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }
}
