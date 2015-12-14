package dao;

import com.avaje.ebean.Model.Finder;
import models.Group;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class GroupDAO {
    private static Finder<Long, Group> find = new Finder<>(Group.class);

    public static List<Group> getAll() {
        return find.all();
    }

    public static Group getById(long id) {
        return find.byId(id);
    }
}
