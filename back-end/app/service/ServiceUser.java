package service;

import dao.InventoryDAO;
import dao.ScrollCollectionDAO;
import forms.Login;
import helper.HMSAccessor;
import models.Scroll;
import models.ShopItem;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;

/**
 * @author fabiomazzone
 */
public class ServiceUser {

    public static User authenticate(Login login) {
        if(login == null) {
            return null;
        }

        User user = dao.UserDAO.getByEmail(login.getEmail());
        if (user != null && BCrypt.checkpw(login.getPassword(), user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     *  updates the isStudent flag
     *
     * @param user asd
     * @return asd
     */
    public static boolean updateStudentState(User user) {
        HMSAccessor hms = new HMSAccessor();
        if (hms.identifyUser(user.getEmail())) {
            user.setStudent(true);

            user.setyID(hms.getResults().get("ynumber"));
            user.setMatNR(hms.getResults().get("matnumber"));
            return true;
        }
        return false;
    }

    public static void resetStory(User user) {
        Logger.info("Profile Reset !!!!");
        user.setCurrentStory(null);
        user.setCurrentScroll(null);
        user.setTutorialDone(false);
        ScrollCollectionDAO.reset(user);
        InventoryDAO.reset(user);
        user.setDepth(0);
    }

    public static void addCurrentScroll(User user) {
        Scroll scroll = user.getCurrentScroll();
        if(scroll.isRecipe()) {
            InventoryDAO.create(user, scroll.getPotion());
        }  else {
            ScrollCollectionDAO.setActive(user, scroll);
        }
    }

    public static boolean buyShopItem(User user, ShopItem shopItem) {
        if(user.getCoins() >= shopItem.getPrice()){
            user.setCoins(user.getCoins() - shopItem.getPrice());
            user.addShopItem(shopItem);
            user.save();
            return true;
        } else {
            return false;
        }
    }

    public static void addScroll(User user, Scroll scroll) {
        user.setScrollLimit(ScrollCollectionDAO.getLimit(user));
        if(user.getScrollLimit() > 0) {
            ScrollCollectionDAO.add(user, scroll);
            user.setScrollLimit(ScrollCollectionDAO.getLimit(user));
        }
    }
}
