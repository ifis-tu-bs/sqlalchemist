package dao;

import helper.Random;

import models.Profile;
import models.SolvedTask;
import models.Task;

import play.Logger;

import java.util.List;

public class TaskDAO {
  //////////////////////////////////////////////////
  //  Object Getter Methods
  //////////////////////////////////////////////////

      /**
       *
       * @param id          the id of the task object
       * @return            the task object
       */
      public static Task getById(long id) {
          return Task.find.byId(id);
      }


    /**
     *
     * @param profile       the profile
     * @param points        the difficulty
     * @return              an task object that matches to the given parameter
     */
      public static Task getByDifficulty(Profile profile, int points) {
          List<Task> TaskList           = Task.find.where().eq("points", points).findList(); //.eq("is_home_work", false).findList();
          List<SolvedTask> solvedTasks  = SolvedTaskDAO.getAllDoneTask(profile);

          if(TaskList == null || TaskList.size() == 0) {
              Logger.warn("Task.getByDifficulty - No isAvailable Tasks found");
              return null;
          }

          if(solvedTasks == null || solvedTasks.size() == 0) {
              return TaskList.get(Random.randomInt(TaskList.size() - 1));
          }
          for(SolvedTask solvedTask : solvedTasks) {
              if(TaskList.contains(solvedTask.getTask())) {
                  TaskList.remove(solvedTask.getTask());
              }
          }
          if(TaskList.size() == 0) {
              TaskList           = Task.find.where().eq("available", true).eq("points", points).findList();
          }

          int i = Random.randomInt(TaskList.size() - 1);

          return TaskList.get(i);
      }

      /**
       *
       *
       * @return    returns a list of all task objects
       */
      public static List<Task> getAll() {
          List<Task> TaskList = Task.find.all();
          if (TaskList == null) {
              Logger.warn("Task.getAll - no entitys saved");
              return null;
          }
          return TaskList;
      }
}
