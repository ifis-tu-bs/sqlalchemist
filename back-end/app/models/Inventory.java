package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Class for the user-inventory
 * @author gabrielahlers
 * @version 1.0
 */
@Entity
@Table (name = "inventory")
public class Inventory extends Model{
    @Id
    private long id;

    @ManyToOne
    private final User user;

    @ManyToOne
    private final Potion potion;

    private int beltSlot;

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Constructor of the inventory
     * @param user Connection to the user profile
     * @param potion Connection to the potion in the slot
     */
    public Inventory(
            User user,
            Potion potion){
        super();

        this.user       = user;
        this.potion     = potion;
        this.beltSlot   = 0;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Potion getPotion() {
        return potion;
    }

    public int getBeltSlot() {
        return beltSlot;
    }

    public void setBeltSlot(int beltSlot) {
        this.beltSlot = beltSlot;
    }
}
