package controllers;

import dao.ShopItemDAO;
import dao.UserDAO;

import models.ShopItem;
import models.User;

import play.libs.Json;
import secured.UserAuthenticator;
import service.ServiceUser;

import view.ShopItemView;


import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.util.List;

/**
 * @author Stefan Hanisch
 */
@Authenticated(UserAuthenticator.class)
public class ShopController extends Controller {

    /**
     * GET /shop/avatar
     *
     * @return ok
     */
    public Result avatarList() {
        User user = UserDAO.getBySession(request().username());

        List<ShopItem> shopItems = ShopItemDAO.getAvatarList();

        return ok(ShopItemView.toJson(user, shopItems));
    }

    /**
     * GET /avatar/belt
     *
     * @return ok
     */
    public Result beltList() {
        User user = UserDAO.getBySession(request().username());

        List<ShopItem> shopItems = ShopItemDAO.getBeltList();

        return ok(ShopItemView.toJson(user, shopItems));
    }

    /**
     * GET /shop/:id
     *
     * @return ok
     */
    public Result buy(Long id) {
        User user = UserDAO.getBySession(request().username());
        ShopItem shopItem = ShopItemDAO.getById(id);

        if(shopItem == null) {
            Logger.warn("ShopController.buy - No ShopItem found");
            return badRequest("No ShopItem found");
        }

        // Check, if user bought this item in the past
        if(user.getShopItems().contains(shopItem)) { // user bought it before
            Logger.warn("ShopController.buy - ShopItem already bought");
            return badRequest("ShopItem already bought");
        } else {
            if (ServiceUser.buyShopItem(user, shopItem)) {
                user.update();
                return ok(Json.toJson(user));
            } else {
                Logger.warn("ShopController.buy - Not enough coins");
                return badRequest("Not enough coins");
            }
        }
    }
}
