package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "loficoinflowlog")
public class LofiCoinFlowLog extends Model {

    @Id
    public long id;

    @ManyToOne
    private final User user;

    private final int lofiCoinsCollected;

    private final Calendar collected;

    public LofiCoinFlowLog(User user, int coins) {
        this.user = user;
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
