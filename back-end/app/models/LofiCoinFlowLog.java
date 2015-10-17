package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "LofiCoinFlowLog")
public class LofiCoinFlowLog extends Model {

    @Id
    public long id;

    @ManyToOne
    private final Profile profile;

    private final int lofiCoinsCollected;

    private final Calendar collected;

    public static final Finder<Long, LofiCoinFlowLog> find = new Finder<>(Long.class, LofiCoinFlowLog.class);

    public LofiCoinFlowLog(Profile profile, int coins) {
        this.profile = profile;
        this.lofiCoinsCollected = coins;

        this.collected = Calendar.getInstance();
    }

    public Calendar getCollected() {
        return this.collected;
    }

    public int getLofiCoinsCollected() {
        return this.lofiCoinsCollected;
    }
}
