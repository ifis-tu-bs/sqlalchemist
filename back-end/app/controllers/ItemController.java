package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Profile;
import models.StoryChallenge;
import models.Scroll;
import models.ScrollCollection;
import models.Inventory;
import models.Potion;
import dao.ProfileDAO;
import dao.InventoryDAO;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;


/**
 * @author fabiomazzone
 */
@Authenticated(secured.UserSecured.class)
public class ItemController extends Controller {

    /**
     * GET /profile/collected
     *
     * @return ok
     */
    public static Result collected() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode body = request().body().asJson();
        StoryChallenge challenge = StoryChallenge.getForProfile(profile);

        if( profile == null) {
            Logger.warn("ItemController.collected - not a valid User");
            badRequest("not a valid User");
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
        int calcLevel   = (depth - 1)  / 5;

        Logger.info("calcLevel: " + calcLevel + " | currentLevel: " + currentLevel);

        if(calcLevel > currentLevel) {
            Logger.info("ItemController set next");
            profile.setCurrentStory(challenge.next);
        }


        Logger.info(body.toString());

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        for(int i = 0; i < scrolls.size(); i++) {
            JsonNode singleScroll = scrolls.get(i);
            int posId = singleScroll.asInt();
            Scroll scroll = Scroll.getByPosId(posId);
            if (scroll != null && !ScrollCollection.contains(profile, scroll)) {
                profile.addScroll(scroll);
                arrayNode.add(scroll.toJson());
            }
        }

        int coinsDifference = profile.addScore(score * 50);
        profile.addRun();
        profile.setDepth(depth);
        profile.update();

        ObjectNode node = Json.newObject();

        node.put("scrolls",     arrayNode);
        node.put("coins",       coinsDifference);

        return ok(node);
    }

    /**
     * GET /profile/scrolls
     *
     * @return ok
     */

    public static Result scrollCollection() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        if(profile == null) {
            Logger.warn("ProfileController.scrollCollection - No profile found");
            return badRequest("no profile found");
        }
        return ok(ScrollCollection.toJsonAll(profile));
    }

    /**
     * GET /profile/belt
     *
     * @return ok
     */
    public static Result belt() {
        Profile profile = ProfileDAO.getByUsername(request().username());

        return ok(InventoryDAO.getJson_Belt(profile));
    }


    /**
     * POST /profile/belt
     *
     * @return ok
     */
    public static Result edit() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode belt = request().body().asJson().path("slots");

        if (belt == null) {
            Logger.warn("ItemController.edit - could not retrieve Json from POST body");
            return badRequest("could not retrieve Json from POST body");
        }
        InventoryDAO.clearBelt(profile);

        int size = belt.size();
        for(int i = 1; i <= size; i++) {
            JsonNode beltSlot = belt.get(i-1);
            int id = beltSlot.findPath("potion").asInt();
            Potion potion = Potion.getById(id);
            if (potion != null) {
                InventoryDAO.updateBeltSlot(profile, potion, i);
            }
        }
        return ok(InventoryDAO.getJson_Belt(profile));
    }

    /**
     * GET /profile/used/:id
     *
     * @return ok
     */
    public static Result used(int id) {
        Profile profile = ProfileDAO.getByUsername(request().username());

        Inventory inventory = InventoryDAO.getBeltSlot(profile, id);
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
    public static Result inventory() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        return ok(InventoryDAO.getJson_Inventory(profile));
    }
}
