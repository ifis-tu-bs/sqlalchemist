package dao;

import com.avaje.ebean.Model;
import models.LofiCoinFlowLog;

import models.User;

import java.util.Calendar;
import java.util.List;

public class LofiCoinFlowLogDAO {
    private static final Model.Finder<Long, LofiCoinFlowLog> find = new Model.Finder<>(LofiCoinFlowLog.class);

    public static int getCollectedCoinsSinceYesterday(User user) {
        List<LofiCoinFlowLog> LogList = find.where().eq("user", user).findList();

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        int collected = 0;

        for(LofiCoinFlowLog lofiCoinFlowLog : LogList) {
            if(yesterday.before(lofiCoinFlowLog.getCollected())) {
                collected = collected + lofiCoinFlowLog.getLofiCoinsCollected();
            }
        }

        return collected;
    }

    public static void add(User user, int coins) {
        LofiCoinFlowLog lofiCoinFlowLog = new LofiCoinFlowLog(user, coins);

        lofiCoinFlowLog.save();
    }
}
