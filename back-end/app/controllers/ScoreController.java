package controllers;

import dao.UserDAO;

import models.User;

import secured.UserAuthenticator;
import service.ServiceScore;

import view.ScoreView;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import java.util.List;

@Authenticated(UserAuthenticator.class)
public class ScoreController extends Controller {
    /**
     * GET      /highscore/points
     *
     * @return  returns the Json.HighScoreList
     */
    public Result byPoints() {
        User user = UserDAO.getBySession(request().username());
        List<User> users = ServiceScore.sortByScore(user);
        return ok(ScoreView.toJsonAll(users, user));
    }
    /**
     * GET      /highscore/time
     *
     * @return  returns the Json.HighScoreList
     */
    public Result byTime() {
        User user = UserDAO.getBySession(request().username());
        List<User> users = ServiceScore.sortByTime(user);
        return ok(ScoreView.toJsonAll(users, user));
    }
    /**
     * GET      /highscore/runs
     *
     * @return  returns the Json.HighScoreList
     */
    public Result byRuns() {
        User user = UserDAO.getBySession(request().username());
        List<User> users = ServiceScore.sortByRuns(user);
        return ok(ScoreView.toJsonAll(users, user));
    }

    /**
     * GET      /highscore/sql
     *
     * @return  returns the Json.HighScoreList
     */
    public Result bySQL() {
        User user = UserDAO.getBySession(request().username());
        List<User> users = ServiceScore.sortBySQL(user);
        return ok(ScoreView.toJsonAll(users, user));
    }

    /**
     * GET      /highscore/rate
     *
     * @return  returns the Json.HighScoreList
     */
    public Result byRate() {
        User user = UserDAO.getBySession(request().username());
        List<User> users = ServiceScore.sortByRate(user);
        return ok(ScoreView.toJsonAll(users, user));
    }

    /**
     * GET      /highscore/coins
     *
     * @return  returns the Json.HighScoreList
     */
    public Result byCoins() {
        User user = UserDAO.getBySession(request().username());
        List<User> users = ServiceScore.sortByCoins(user);
        return ok(ScoreView.toJsonAll(users, user));
    }
}
