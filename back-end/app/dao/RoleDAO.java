package dao;

import com.avaje.ebean.Model;
import models.Role;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class RoleDAO {
    private static Model.Finder<Long, Role> find = new Model.Finder<>(Role.class);

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

    public static Role getByPriority(int priority) {
        return find.query().where().eq("priority", priority).findUnique();
    }

    public static Role getByRoleName(String roleName) {
        return find.query().where().eq("roleName", roleName).findUnique();
    }

    public static Role getLowerRole(int priority) {
        List<Role> roles = find.query().where().lt("priority", priority).order().desc("priority").findList();
        if(roles == null || roles.size() == 0 ) {
            return null;
        }
        return roles.get(0);
    }

    public static Role getNextHigherRole(int priority) {
        List<Role> roles = find.query().where().gt("priority", priority).order().asc("priority").findList();
        if(roles == null || roles.size() == 0 ) {
            return null;
        }
        return roles.get(0);
    }
}
