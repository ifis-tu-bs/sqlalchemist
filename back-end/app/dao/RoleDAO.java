package dao;

import com.avaje.ebean.Finder;
import models.Role;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class RoleDAO {
    private static Finder<Long, Role> find = new Finder<Long, Role>(Role.class);

    public static List<Role> getAll() {
        return find.all();
    }

    public static Role getById(long id) {
        return find.byId(id);
    }

    public static Role getUser() {
        return find.query().where().eq("priority", 10).findUnique();
    }

    public static Role getAdmin() {
        return find.query().where().eq("priority", 10000).findUnique();
    }
}
