package dao;

import com.avaje.ebean.Model.Finder;
import models.UserGroup;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class UserGroupDAO {
    private static Finder<Long, UserGroup> find = new Finder<>(UserGroup.class);

    public static List<UserGroup> getAll() {
        return find.all();
    }

    public static UserGroup getById(long id) {
        return find.byId(id);
    }
}
