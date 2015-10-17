package controllers;

import dao.ProfileDAO;
import dao.StoryChallengeDAO;

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

    public Result story() {
        Profile profile = ProfileDAO.getByUsername(request().username());

        StoryChallenge challenge = StoryChallengeDAO.getForProfile(profile);

        return ok(challenge.toJson());
    }

    public Result skipChallenge() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        profile.setTutorialDone(true);

        profile.setCurrentStory(StoryChallengeDAO.getFirstLevel());

        profile.update();
        return ok();
    }

    public Result reset() {
        Profile profile = ProfileDAO.getByUsername(request().username());

        profile.setCurrentStory(StoryChallengeDAO.getFirstLevel());
        profile.setTutorialDone(false);

        profile.update();

        return ok();
    }
}
