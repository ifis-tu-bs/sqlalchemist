package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("sessionID")
    public String getId() {
        return id;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @JsonProperty("owner")
    public String getOwnerName() {
        return (this.owner != null)? this.owner.getUsername(): "";
    }

    @JsonIgnore
    public Calendar getCreatedAt() {
        return createdAt;
    }

    @JsonIgnore
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
