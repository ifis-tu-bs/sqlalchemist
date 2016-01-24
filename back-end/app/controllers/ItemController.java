package controllers;

import dao.*;

import models.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import secured.UserAuthenticator;
import service.ServiceScore;
import service.ServiceUser;


/**
 * @author fabiomazzone
 */
@Authenticated(UserAuthenticator.class)
public class ItemController extends Controller {

    /**
     * GET /profile/collected
     *
     * @return ok
     */
    public Result collected() {
        User user = UserDAO.getBySession(request().username());
        JsonNode body = request().body().asJson();
        StoryChallenge challenge = StoryChallengeDAO.getForUser(user);

        if( user == null) {
            Logger.warn("ItemController.collected - not a valid User");
            return badRequest("not a valid User");
        }

        if (body == null) {
            Logger.warn("ItemController.collected - Could not retrieve Json from POST body!");
            return badRequest("Could not retrieve Json from POST body!");
        }
        int             depth       = body.path("depth").intValue();
        int             score       = body.path("score").intValue();
        JsonNode        scrolls     = body.path("scrolls");

        if(scrolls == null) {
            Logger.warn("ItemController.collected - No Path for scrolls found");
            return badRequest("No Path for scrolls found");
        }

        int currentLevel = challenge.getLevel();
        int calcLevel    = (depth - 1) / 5;

        if(depth > 1 && !user.isTutorialDone()) {
            user.setTutorialDone(true);
            user.setCurrentStory(challenge.getNext());
        }

        if(calcLevel > currentLevel) {
            user.setCurrentStory(challenge.getNext());
        }

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        for(int i = 0; i < scrolls.size(); i++) {
            JsonNode singleScroll = scrolls.get(i);
            int posId = singleScroll.asInt();
            Scroll scroll = ScrollDAO.getByPosId(posId);
            if (scroll != null && !ScrollCollectionDAO.contains(user, scroll)) {
                ServiceUser.addScroll(user, scroll);
                arrayNode.add(scroll.toJson());
            }
        }

        int coinsDifference = ServiceScore.addScore(user, score * 50);
        ServiceScore.addRun(user);
        if(depth > user.getDepth()) {
            user.setDepth(depth);
        }
        user.update();

        ObjectNode node = Json.newObject();

        node.set("scrolls",     arrayNode);
        node.put("coins",       coinsDifference);

        return ok(node);
    }

    /**
     * GET /profile/scrolls
     *
     * @return ok
     */

    public Result scrollCollection() {
        User user = UserDAO.getBySession(request().username());
        if(user == null) {
            Logger.warn("ProfileController.scrollCollection - No profile found");
            return badRequest("no profile found");
        }
        return ok(ScrollCollection.toJsonAll(user));
    }

    /**
     * GET /profile/belt
     *
     * @return ok
     */
    public Result belt() {
        User user = UserDAO.getBySession(request().username());

        return ok(InventoryDAO.getJson_Belt(user));
    }


    /**
     * POST /profile/belt
     *
     * @return ok
     */
    public Result edit() {
        User user = UserDAO.getBySession(request().username());
        JsonNode belt = request().body().asJson().path("slots");

        if (belt == null) {
            Logger.warn("ItemController.edit - could not retrieve Json from POST body");
            return badRequest("could not retrieve Json from POST body");
        }
        InventoryDAO.clearBelt(user);

        int size = belt.size();
        for(int i = 1; i <= size; i++) {
            JsonNode beltSlot = belt.get(i-1);
            int id = beltSlot.findPath("potion").asInt();
            Potion potion = PotionDAO.getById(id);
            if (potion != null) {
                InventoryDAO.updateBeltSlot(user, potion, i);
            }
        }
        return ok(InventoryDAO.getJson_Belt(user));
    }

    /**
     * GET /profile/used/:id
     *
     * @return ok
     */
    public Result used(int id) {
        User user = UserDAO.getBySession(request().username());

        Inventory inventory = InventoryDAO.getBeltSlot(user, id);
        if(inventory == null){
            Logger.warn("ItemController.used - no beltSlot for profile and id found");
            return badRequest("no beltSlot for profile and id found");
        }
        inventory.delete();

        return ok();
    }

    /**
     * POST /profile/collected
     *
     * @return ok
     */
    public Result inventory() {
        User user = UserDAO.getBySession(request().username());
        return ok(InventoryDAO.getJson_Inventory(user));
    }
}
