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
    public final Profile profile;

    @ManyToOne
    public final Potion potion;

    private int beltSlot;

    public static final Finder<Long, Inventory> find = new Finder<>(Inventory.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Constructor of the inventory
     * @param profile Connection to the user profile
     * @param potion Connection to the potion in the slot
     */
    public Inventory(
            Profile profile,
            Potion potion){
        super();

        this.profile    = profile;
        this.potion     = potion;
        this.beltSlot   = 0;
    }




    public void setBeltSlot(int beltSlot) {
        this.beltSlot = beltSlot;
    }
}
