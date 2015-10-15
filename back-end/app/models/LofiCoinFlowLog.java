package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author fabiomazzone
 */
@Entity
@Table
public class LofiCoinFlowLog extends Model {

    @Id
    public long id;

    @ManyToOne
    public Profile profile;

    public int lofiCoinsCollected = 0;

    public Calendar collected;

    public static final Finder<Long, LofiCoinFlowLog> find = new Finder<>(Long.class, LofiCoinFlowLog.class);

    public LofiCoinFlowLog(Profile profile, int coins) {
        this.profile = profile;
        this.lofiCoinsCollected = coins;

        this.collected = Calendar.getInstance();
    }
}
