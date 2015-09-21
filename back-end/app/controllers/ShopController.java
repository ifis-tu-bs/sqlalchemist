package controllers;

import models.Profile;
import models.ShopItem;
import models.User;
import dao.UserDAO;
import dao.ProfileDAO;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.util.List;

/**
 * @author Stefan Hanisch
 */
@Authenticated(secured.UserSecured.class)
public class ShopController extends Controller {

    /**
     * GET /shop
     * GET /shop/avatar
     *
     * @return ok
     */
    public static Result avatarList() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<ShopItem> shopItems = ShopItem.getAvatarList();

        return ok(ShopItem.toJsonAll(profile, shopItems));
    }

    /**
     * GET /avatar/belt
     *
     * @return ok
     */
    public static Result beltList() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<ShopItem> shopItems = ShopItem.getBeltList();

        return ok(ShopItem.toJsonAll(profile, shopItems));
    }

    /**
     * GET /shop/:id
     *
     * @return ok
     */
    public static Result buy(Long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        ShopItem shopItem = ShopItem.getById(id);

        if(profile == null || shopItem == null) {
            Logger.warn("ShopController.buy - No ShopItem or Profile found");
            return badRequest("No ShopItem or Profile found");
        }
        // Check, if user bought this item in the past
        if(profile.shopItemInList(shopItem)) { // user bought it before
            Logger.warn("ShopController.buy - ShopItem already bought");
            return badRequest("ShopItem already bought");
        } else {
            if (profile.buy(shopItem)) {
                profile.update();
                return ok(profile.toJsonPlayerState());
            } else {
                Logger.warn("ShopController.buy - Not enough coins");
                return badRequest("Not enough coins");
            }
        }
    }
}
