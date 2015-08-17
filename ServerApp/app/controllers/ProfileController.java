package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.Logger;
import play.libs.Json;
import play.mvc.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 *
 * @author fabiomazzone
 */
@Security.Authenticated(secured.UserSecured.class)
public class ProfileController extends Controller {

    /**
     * GET      /profile
     *
     * @return returns the PlayerState as JSON Object
     */
    public static Result index() {
        Profile profile = User.getProfile(session());

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
        Profile profile = Profile.getById(id);

        return ok(profile.toJsonProfile());
    }

    /**
     * GET      /profile/character
     *
     * @return  returns the CharacterState as JSON Object
     */
    public static Result character() {
        Profile profile = User.getProfile(session());

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
        Profile profile = User.getProfile(session());

        if(profile == null || !profile.setAvatar(id)) {
            Logger.warn("ProfileController.avatar - No profile found or no Avatar Found");
            return badRequest("No profile found or no Avatar Found");
        }
        profile.update();
        return ok(profile.toJsonCharacterState());
    }


    public static Result reset() {
        Profile profile = User.getProfile(session());

        profile.resetStory();

        profile.update();

        return ok();
    }

    public static Result getUserHomeworks() {
        List<Object> submits = SubmittedHomeWork.getSubmitsForProfile(User.getProfile(session()));
        List<HomeWorkChallenge> homeWorks = HomeWorkChallenge.getHomeWorksForSubmits(submits);
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
