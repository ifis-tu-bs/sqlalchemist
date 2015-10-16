package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;


import javax.persistence.*;
import java.util.Date;


/**
 *  asd
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "UserSession")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class UserSession extends Model {
    // unique ID
    @Id
    public Long id;

    @Column(name = "sessionID", unique = true, length=30)
    @Constraints.Required
    private String sessionID;

    @ManyToOne(cascade = CascadeType.ALL)
    public User user;

    @Column(name = "remoteAddress")
    @Constraints.Required
    private String remoteAddress;

    @Column(name = "created_at")
    @Constraints.Required
    private Date created_at;

    @Column(name = "expires_at")
    @Constraints.Required
    private Date expires_at;

    /**
     * Need for Database Communication
     */
    public static final Finder<Long,UserSession> find = new Finder<>(
            Long.class, UserSession.class
    );

    /**
     *
     * @param user wants one valid user
     * @param duration duration in days
     * @return returns the created and saved Session Object
     */
    public static UserSession create(User user, int duration, String remoteAddress) {
        if(duration < 1) {
            return null;
        }
        UserSession session = new UserSession();
        session.user = user;
        session.created_at = new Date();
        session.expires_at = new Date(session.created_at.getTime() + duration*((1000 * 60 * 60 * 24)));
        session.sessionID = Integer.toHexString(session.hashCode());
        session.remoteAddress = remoteAddress;
        session.save();
        return session;
    }

    /**
     *
     * @return returns the Session token of the current Object
     */
    public String getSessionID() {
        return this.sessionID;
    }

    public User getUser() {
        return this.user;
    }

    /**
     *
     * @return returns boolean value
     */
    public boolean isValid(String remoteAddress) {
        Date today = new Date();

        return this.expires_at.compareTo(today) > 0 && this.remoteAddress.equals(remoteAddress);
    }
}
