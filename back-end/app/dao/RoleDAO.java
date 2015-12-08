package dao;

import com.avaje.ebean.Finder;
import models.ActionRule;
import models.Role;
import models.User;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * @author fabiomazzone
 */
public class RoleDAO {
    private static Finder<Long, Role> find = new Finder<Long, Role>(Role.class);

    public static Role create(
            int         priority,
            String      roleName,
            ActionRule ownTaskSet,
            ActionRule foreignTaskSet,
            ActionRule ownTask,
            ActionRule foreignTask,
            ActionRule homework,
            ActionRule role,
            ActionRule user,
            boolean     isDeletable,
            User        creator) {

        Role roleObj = new Role(
                priority,
                roleName,
                ownTaskSet,
                foreignTaskSet,
                ownTask,
                foreignTask,
                homework,
                role,
                user,
                isDeletable,
                creator);

        try {
            roleObj.save();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }

        return roleObj;
    }

    public static List<Role> getAll() {
        return find.all();
    }

    public static Role getUser() {
        return find.query().where().eq("priority", 100).findUnique();
    }
}
