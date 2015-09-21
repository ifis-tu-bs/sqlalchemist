package controllers;

import models.Profile;

import dao.ProfileDAO;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import secured.UserSecured;

import java.util.List;

@Authenticated(UserSecured.class)
public class HighScoreController extends Controller {
    /**
     * GET      /highscore/points
     *
     * @return  returns the Json.HighScoreList
     */
    public static Result byPoints() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<Profile> profiles = Profile.sortByScore(profile);
        return ok(Profile.toJsonHighScoreAll(profiles, profile));
    }
    /**
     * GET      /highscore/time
     *
     * @return  returns the Json.HighScoreList
     */
    public static Result byTime() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<Profile> profiles = Profile.sortByTime(profile);
        return ok(Profile.toJsonHighScoreAll(profiles, profile));
    }
    /**
     * GET      /highscore/runs
     *
     * @return  returns the Json.HighScoreList
     */
    public static Result byRuns() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<Profile> profiles = Profile.sortByRuns(profile);
        return ok(Profile.toJsonHighScoreAll(profiles, profile));
    }

    /**
     * GET      /highscore/sql
     *
     * @return  returns the Json.HighScoreList
     */
    public static Result bySQL() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<Profile> profiles = Profile.sortBySQL(profile);
        return ok(Profile.toJsonHighScoreAll(profiles, profile));
    }

    /**
     * GET      /highscore/rate
     *
     * @return  returns the Json.HighScoreList
     */
    public static Result byRate() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<Profile> profiles = Profile.sortByRate(profile);
        return ok(Profile.toJsonHighScoreAll(profiles, profile));
    }

    /**
     * GET      /highscore/coins
     *
     * @return  returns the Json.HighScoreList
     */
    public static Result byCoins() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<Profile> profiles = Profile.sortByCoins(profile);;
        return ok(Profile.toJsonHighScoreAll(profiles, profile));
    }
}
