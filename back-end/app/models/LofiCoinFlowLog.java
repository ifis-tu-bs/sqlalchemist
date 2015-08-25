package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table
public class LofiCoinFlowLog extends Model {

    @Id
    long id;

    @ManyToOne
    Profile profile;

    int lofiCoinsCollected = 0;

    Calendar collected;

    private static final Finder<Long, LofiCoinFlowLog> find = new Finder<>(Long.class, LofiCoinFlowLog.class);

    public LofiCoinFlowLog(Profile profile, int coins) {
        this.profile = profile;
        this.lofiCoinsCollected = coins;

        this.collected = Calendar.getInstance();
    }


    public static int getCollectedCoinsSinceYesterday(Profile profile) {
        List<LofiCoinFlowLog> LogList = find.where().eq("profile", profile).findList();

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        int collected = 0;

        for(LofiCoinFlowLog lofiCoinFlowLog : LogList) {
            if(yesterday.before(lofiCoinFlowLog.collected)) {
                collected = collected + lofiCoinFlowLog.lofiCoinsCollected;
            }
        }

        return collected;
    }

    public static void add(Profile profile, int coins) {
        LofiCoinFlowLog lofiCoinFlowLog = new LofiCoinFlowLog(profile, coins);

        lofiCoinFlowLog.save();
    }
}
