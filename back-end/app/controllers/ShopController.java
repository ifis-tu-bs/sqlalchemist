package controllers;

import dao.ProfileDAO;
import dao.ShopItemDAO;

import models.Profile;
import models.ShopItem;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import view.ShopItemView;

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
    public Result avatarList() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<ShopItem> shopItems = ShopItemDAO.getAvatarList();

        return ok(ShopItemView.toJson(profile, shopItems));
    }

    /**
     * GET /avatar/belt
     *
     * @return ok
     */
    public Result beltList() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        List<ShopItem> shopItems = ShopItemDAO.getBeltList();

        return ok(ShopItemView.toJson(profile, shopItems));
    }

    /**
     * GET /shop/:id
     *
     * @return ok
     */
    public Result buy(Long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        ShopItem shopItem = ShopItemDAO.getById(id);

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
