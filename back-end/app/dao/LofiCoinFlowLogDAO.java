package dao;

import models.LofiCoinFlowLog;
import models.Profile;

import java.util.Calendar;
import java.util.List;

public class LofiCoinFlowLogDAO {
  public static int getCollectedCoinsSinceYesterday(Profile profile) {
      List<LofiCoinFlowLog> LogList = LofiCoinFlowLog.find.where().eq("profile", profile).findList();

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

  public static void add(Profile profile, int coins) {
      LofiCoinFlowLog lofiCoinFlowLog = new LofiCoinFlowLog(profile, coins);

      lofiCoinFlowLog.save();
  }
}
