package dao;

import models.TaskSet;

import play.Logger;

import java.util.List;

public class TaskSetDAO {

  //////////////////////////////////////////////////
  //  object find methods
  //////////////////////////////////////////////////

    /**
    * get a list of all TaskSet objects
    * @return returns a list of all TaskSet objects
    */
    public static List<TaskSet> getAll()
    {
        List<TaskSet> taskSetList = TaskSet.find.all();

        if (taskSetList == null)
        {
            Logger.warn("TaskSetDAO.getAll - no TaskSet Objects-Found");
            return null;
        }
        return taskSetList;
    }

    /**
     * get a list of all TaskSet objects
     * @return returns a list of all TaskSet objects
     */
    public static List<TaskSet> getAllHomeWorkTaskSets()
    {
        List<TaskSet> taskSetList = TaskSet.find.where().eq("isHomework", true).findList();

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
    public static TaskSet getById(Long id)
    {
        TaskSet taskSet = TaskSet.find.byId(id);

        if (taskSet == null)
        {
            Logger.warn("TaskSetDAO.getById("+id+") - no TaskSet found");
            return null;
        }
        return taskSet;
    }


}
