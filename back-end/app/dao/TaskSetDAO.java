package dao;

import com.avaje.ebean.Model;
import models.TaskSet;

import models.User;
import play.Logger;

import java.util.List;

public class TaskSetDAO {
    private static final Model.Finder<Long, TaskSet> find = new Model.Finder<>(TaskSet.class);
  //////////////////////////////////////////////////
  //  object find methods
  //////////////////////////////////////////////////

    public static List<TaskSet> getAll() {
        return find.all();
    }
    /**
    * get a list of all TaskSet objects
    * @return returns a list of all TaskSet objects
    */
    public static List<TaskSet> getAllExceptHomeworks() {
        return find.where().eq("isHomework", false).findList();
    }

    /**
     * get a list of all TaskSet objects
     * @return returns a list of all TaskSet objects
     */
    public static List<TaskSet> getAllHomeWorkTaskSets() {
        List<TaskSet> taskSetList = find.where().eq("isHomework", true).findList();

        if (taskSetList == null)
        {
            Logger.warn("TaskSetDAO.getAll - no TaskSet Objects-Found");
            return null;
        }
        return taskSetList;
    }

    /**
    * This method finds the TaskSet with the given id
    *
    * @param id    the id of a TaskSet as Long
    * @return      returns the matching TaskSet or null
    */
    public static TaskSet getById(Long id) {
        TaskSet taskSet = find.byId(id);

        if (taskSet == null)
        {
            Logger.warn("TaskSetDAO.getById("+id+") - no TaskSet found");
            return null;
        }
        return taskSet;
    }


    public static List<TaskSet> getAllExceptOwn(User user) {
        return find.where().eq("isHomework", false).ne("creator", user).findList();
    }

    public static List<TaskSet> getAllOwn(User user) {
        return find.where().eq("isHomework", false).eq("creator", user).findList();
    }
}
