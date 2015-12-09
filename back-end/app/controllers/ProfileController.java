package controllers;

import dao.*;

import models.Avatar;
import models.HomeWork;
import models.User;

import secured.UserAuthenticator;

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
@Authenticated(UserAuthenticator.class)
public class ProfileController extends Controller {

    /**
     * GET      /profile
     *
     * @return returns the PlayerState as JSON Object
     */
    public Result read() {
        User user = UserDAO.getBySession(request().username());

        if(user == null){
            Logger.warn("ProfileController - no profile found");
            return badRequest("no profile found");
        }

        return ok(user.toJsonPlayerState());
    }

    /**
     * GET      /profile/:id
     *
     * @param   id profile id
     * @return  returns the Profile as JSON Object
     */
    public Result view(Long id) {
        User user = UserDAO.getBySession(request().username());

        return ok(user.toJsonProfile());
    }

    /**
     * GET      /profile/character
     *
     * @return  returns the CharacterState as JSON Object
     */
    public Result character() {
        User user = UserDAO.getBySession(request().username());

        if(user == null) {
            Logger.warn("ProfileController.character - No profile found");
            return badRequest("No profile found");
        }

        return ok(user.toJsonCharacterState());
    }

    /**
     * GET      /profile/avatar/:id
     *
     * @param   id avatar id
     * @return  returns the new playerStats
     */
    public Result avatar(long id) {
        User user = UserDAO.getBySession(request().username());
        Avatar avatar = AvatarDAO.getById(id);

        if(user == null) {
            Logger.warn("ProfileController.avatar - No profile found or no Avatar Found");
            return badRequest("No profile found or no Avatar Found");
        }

        if(!user.setAvatar(avatar)) {
            Logger.warn("ProfileController.avatar - No profile found or no Avatar Found");
            return badRequest("No profile found or no Avatar Found");
        }

        user.update();
        return ok(user.toJsonCharacterState());
    }

    public Result getUserHomeworks() {
        User user = UserDAO.getBySession(request().username());
        List<Object> submits = SubmittedHomeWorkDAO.getSubmitsForUser(user);
        List<HomeWork> homeWorks = HomeWorkDAO.getHomeWorksForSubmits(submits);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (HomeWork homeWork : homeWorks) {
            ObjectNode json = Json.newObject();
            json.put("name", homeWork.getHomeWorkName());
            json.put("expires_at", df.format(homeWork.getExpire_at()));
            json.put("submitted", homeWork.submittedAll(submits));

            arrayNode.add(json);
        }

        return ok(arrayNode);
    }
}
