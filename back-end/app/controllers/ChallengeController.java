package controllers;

import models.*;
import dao.UserDAO;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import secured.UserSecured;

/**
 * @author fabiomazzone
 */

@Security.Authenticated(UserSecured.class)
public class ChallengeController extends Controller {

    /**
     * GET    /challenge/story
     * @return
     */
    public static Result story() {
        Profile profile = UserDAO.getProfile(session());

        StoryChallenge challenge = StoryChallenge.getForProfile(profile);

        return ok(challenge.toJson());
    }

    public static Result skipChallenge() {
        Profile profile = UserDAO.getProfile(session());
        profile.setTutorialDone(true);

        profile.setCurrentStory(StoryChallenge.getFirstLevel());

        profile.update();
        return ok();
    }

    public static Result reset() {
        Profile profile = UserDAO.getProfile(session());

        profile.setCurrentStory(StoryChallenge.getFirstLevel());
        profile.setTutorialDone(false);

        profile.update();

        return ok();
    }
}
