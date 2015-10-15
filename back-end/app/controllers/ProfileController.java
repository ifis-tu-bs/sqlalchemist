package controllers;

import dao.HomeWorkChallengeDAO;
import dao.ProfileDAO;
import dao.SubmittedHomeWorkDAO;

import models.HomeWorkChallenge;
import models.Profile;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import play.libs.Json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 *
 * @author fabiomazzone
 */
@Authenticated(secured.UserSecured.class)
public class ProfileController extends Controller {

    /**
     * GET      /profile
     *
     * @return returns the PlayerState as JSON Object
     */
    public static Result read() {
        Logger.info(request().username());
        Profile profile = ProfileDAO.getByUsername(request().username());

        if(profile == null){
            Logger.warn("ProfileController - no profile found");
            return badRequest("no profile found");
        }

        return ok(profile.toJsonPlayerState());
    }

    /**
     * GET      /profile/:id
     *
     * @param   id profile id
     * @return  returns the Profile as JSON Object
     */
    public static Result view(Long id) {
        Profile profile = ProfileDAO.getById(id);

        return ok(profile.toJson());
    }

    /**
     * GET      /profile/character
     *
     * @return  returns the CharacterState as JSON Object
     */
    public static Result character() {
        Profile profile = ProfileDAO.getByUsername(request().username());

        if(profile == null) {
            Logger.warn("ProfileController.character - No profile found");
            return badRequest("No profile found");
        }

        return ok(profile.toJsonCharacterState());
    }

    /**
     * GET      /profile/avatar/:id
     *
     * @param   id avatar id
     * @return  returns the new playerStats
     */
    public static Result avatar(long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());

        if(profile == null || !profile.setAvatar(id)) {
            Logger.warn("ProfileController.avatar - No profile found or no Avatar Found");
            return badRequest("No profile found or no Avatar Found");
        }
        profile.update();
        return ok(profile.toJsonCharacterState());
    }


    public static Result reset() {
        Profile profile = ProfileDAO.getByUsername(request().username());

        profile.resetStory();

        profile.update();

        return ok();
    }

    public static Result getUserHomeworks() {
        List<Object> submits = SubmittedHomeWorkDAO.getSubmitsForProfile(ProfileDAO.getByUsername(request().username()));
        List<HomeWorkChallenge> homeWorks = HomeWorkChallengeDAO.getHomeWorksForSubmits(submits);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (HomeWorkChallenge homeWork : homeWorks) {
            ObjectNode json = Json.newObject();
            json.put("name", homeWork.getName());
            json.put("expires_at", df.format(homeWork.getExpires_at()));
            json.put("submitted", homeWork.submittedAll(submits));

            arrayNode.add(json);
        }

        return ok(arrayNode);
    }
}
