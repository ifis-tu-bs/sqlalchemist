package service;

import dao.LofiCoinFlowLogDAO;
import dao.UserDAO;

import models.Score;
import models.User;

import com.avaje.ebean.Query;
import play.Play;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class ServiceScore {
    private static List<User> getQuery(User user, String query) {
        List<User> ranking;

        Query<User> orderBy = UserDAO.find.orderBy(query);
        user.getScore().setOwnRank(orderBy.findList().indexOf(user));
        ranking = orderBy.setMaxRows(10).findList();

        return ranking;
    }

    public static List<User> sortByScore(User user) {
        return getQuery(user, "total_score desc");
    }

    public static List<User> sortByTime(User user) {
        return getQuery(user, "played_time desc");
    }

    public static List<User> sortByRuns(User user) {
        return getQuery(user, "played_runs desc");
    }

    public static List<User> sortBySQL(User user) {
        return getQuery(user, "solved_sql desc");
    }

    public static List<User> sortByRate(User user) {
        return getQuery(user, "success_rate desc");
    }

    public static List<User> sortByCoins(User user) {
        return getQuery(user, "total_coins desc");
    }

    public static void addSuccessfully(User user) {
        Score score = user.getScore();
        score.setSolvedSQL(score.getSolvedSQL() + 1);
        score.setSuccessRate((int) (((float) score.getSolvedSQL() / (float) score.getDoneSQL()) * 100));
        user.setScore(score);
        /*if (this.solvedSQL > Play.application().configuration().getInt("User.AutoPromote")) {
            User user = this.getUser();
            if (user.getRole() < User.ROLE_CREATOR) {
                user.promote(User.ROLE_CREATOR);
            }
        }*/
    }

    public static void addStatement(User user) {
        Score score = user.getScore();
        score.setDoneSQL(score.getDoneSQL() + 1);
        score.setSuccessRate((int) (((float) score.getSolvedSQL() / (float) score.getDoneSQL()) * 100));
        user.setScore(score);
    }

    public static int addScore(User user, int points) {
        Score score = user.getScore();
        score.setTotalScore( score.getTotalScore() + points);
        user.setScore(score);
        return addCoinsFromScore(user, points);
    }

    private static int addCoinsFromScore(User user, int points) {
        Score score = user.getScore();

        int coins       = points;
        int collected   = LofiCoinFlowLogDAO.getCollectedCoinsSinceYesterday(user);
        collected       = coins + collected;

        int coinLimit = Play.application().configuration().getInt("Game.CoinLimit");
        user.setCoinScale(1 -  ( (float)collected / ((float)coinLimit + (float)collected)));

        coins = (int) (coins  * user.getCoinScale());

        score.setTotalCoins(score.getTotalCoins() + coins);
        user.setCoins(user.getCoins() + coins);
        if(user.getCoins() >= 1000000) {
            user.setCoins(999999);
        }
        LofiCoinFlowLogDAO.add(user, coins);
        user.setScore(score);
        return coins;
    }

    public static void addTime(User user, int time) {
        Score score = user.getScore();
        score.setPlayedTime(score.getPlayedTime() + time);
        user.setScore(score);
    }

    public static void addRun(User user) {
        Score score = user.getScore();
        score.setPlayedRuns(score.getPlayedRuns() + 1);
        user.setScore(score);
    }
}
