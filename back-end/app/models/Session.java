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

    private boolean active;

    private Calendar createdAt;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Constructor
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Session() {
        this.id         = UUID.randomUUID().toString();
        this.active     = true;
        this.createdAt  = Calendar.getInstance();
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

    public boolean isActive() {
        return active;
    }

    public void disable() {
        this.active = false;
    }

    @JsonIgnore
    public Calendar getCreatedAt() {
        return createdAt;
    }
}
