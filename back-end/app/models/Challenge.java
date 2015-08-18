package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "challenge")
public class Challenge extends Model {
    @Id
    long id;

    @Column(name = "challenge_name")
    protected String name;

    public static final int CHALLENGE_SOLVE_POINTS  = 1;
    public static final int CHALLENGE_SOLVE_TIME    = 2;
    public static final int CHALLENGE_SOLVE_SOLVED  = 3;
    private final int solve_type;
    protected final int solve_type_extension;


    protected final Date created_at;

    @Column(name = "type")
    public int type;

    protected Date modified_at = null;



    protected Profile player;

    private static final Finder<Long, Challenge> find = new Finder<>(
            Long.class, Challenge.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////
    /**
     *
     * @param name
     * @param solve_type
     * @param solve_type_extension
     */
    Challenge(
            String name,
            int solve_type,
            int solve_type_extension,
            int type) {
        super();

        this.name = name;
        this.solve_type = solve_type;
        this.solve_type_extension = solve_type_extension;

        this.type = type;

        this.created_at = new Date();
    }


    /**
     *
     */
    @Override
    public void update() {
        this.modified_at = new Date();

        super.update();
    }

}
