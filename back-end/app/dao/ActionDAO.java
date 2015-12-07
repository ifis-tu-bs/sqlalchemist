package dao;

import com.avaje.ebean.Model;
import models.Action;

/**
 * @author fabiomazzone
 */
public class ActionDAO {
    private static Model.Finder<Long, Action> find = new Model.Finder<Long, Action>(Action.class);


    public static Action create(int actionType) {
        Action action = new Action(actionType);

        action.save();

        return action;
    }
}
