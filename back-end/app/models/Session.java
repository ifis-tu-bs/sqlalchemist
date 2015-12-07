package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "session")
public class Session extends Model {
    @Id
    private String id;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "session")
    private List<Action> actions;

    private Calendar createdAt;

    @OneToOne
    private Action lastAction;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Constructor
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Session() {
        this.id         = UUID.randomUUID().toString();
        this.createdAt = Calendar.getInstance();
        this.lastAction = null;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Getter & Setter
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Action getLastAction() {
        return lastAction;
    }

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }

    public void addAction(Action action) {
        action.setSession(this);
        action.save();
        this.actions.add(action);
        this.setLastAction(action);
    }
}
