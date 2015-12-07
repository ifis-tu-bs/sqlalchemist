package controllers;

import dao.StoryChallengeDAO;
import dao.UserDAO;

import models.StoryChallenge;
import models.User;

import secured.UserAuthenticator;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.ServiceUser;

/**
 * @author fabiomazzone
 */
@Security.Authenticated(UserAuthenticator.class)
public class LevelController extends Controller {

    public Result story() {
        User user = UserDAO.getBySession(request().username());

        StoryChallenge challenge = StoryChallengeDAO.getForUser(user);

        return ok(challenge.toJson());
    }

    public Result skipChallenge() {
        User user = UserDAO.getBySession(request().username());

        user.setTutorialDone(true);

        user.setCurrentStory(StoryChallengeDAO.getFirstLevel());

        user.update();
        return ok();
    }

    public Result reset() {
        User user = UserDAO.getBySession(request().username());

        ServiceUser.resetStory(user);

        user.update();

        return ok();
    }
}
