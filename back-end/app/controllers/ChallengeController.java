package controllers;

import dao.ProfileDAO;

import models.Profile;
import models.StoryChallenge;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import secured.UserSecured;

/**
 * @author fabiomazzone
 */

@Authenticated(UserSecured.class)
public class ChallengeController extends Controller {

    /**
     * GET    /challenge/story
     * @return
     */
    public static Result story() {
        Profile profile = ProfileDAO.getByUsername(request().username());

        StoryChallenge challenge = StoryChallenge.getForProfile(profile);

        return ok(challenge.toJson());
    }

    public static Result skipChallenge() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        profile.setTutorialDone(true);

        profile.setCurrentStory(StoryChallenge.getFirstLevel());

        profile.update();
        return ok();
    }

    public static Result reset() {
        Profile profile = ProfileDAO.getByUsername(request().username());

        profile.setCurrentStory(StoryChallenge.getFirstLevel());
        profile.setTutorialDone(false);

        profile.update();

        return ok();
    }
}
