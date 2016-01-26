package dao;

import com.avaje.ebean.Finder;
import models.ForeignKeyRelation;
import models.TaskSet;

import java.util.List;

/**
 * Created by invisible on 1/25/16.
 */
public class ForeignKeyDAO {
    static Finder<Long, ForeignKeyRelation> find = new Finder<>(ForeignKeyRelation.class);

    public static List<ForeignKeyRelation> getAllCombinedRelations(TaskSet taskSet, int combinedKeyId) {
        return find.query().where().eq("taskSet", taskSet).eq("combinedKeyId", combinedKeyId).findList();
    }

    public static List<ForeignKeyRelation> getAllUncombinedRelations(TaskSet taskSet) {
        return find.query().where().eq("taskSet", taskSet).isNull("combinedKeyId").findList();
    }

    public static List<ForeignKeyRelation> getAllCombinedKeyIds(TaskSet taskSet) {
        return find.query().setDistinct(true).select("combinedKeyId").where().eq("taskSet", taskSet).isNotNull("combinedKeyId").findList();
    }
}
