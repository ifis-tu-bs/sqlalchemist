package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "action")
public class Action extends Model{
    @Id
    private Long id;

    public static final int LOGIN = 1;
    private int action;

    @ManyToOne
    private Session session;

    private Calendar date;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Constructor
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Action(int action) {
        this.action = action;

        this.date = Calendar.getInstance();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Getter & Setter
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Long getId() {
        return id;
    }

    public int getAction() {
        return action;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public Calendar getDate() {
        return date;
    }
}
