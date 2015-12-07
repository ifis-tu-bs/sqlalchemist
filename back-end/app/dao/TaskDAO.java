package dao;

import helper.Random;

import models.SolvedTask;
import models.Task;
import models.User;

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
     * @param user       the profile
     * @param difficulty    the difficulty
     * @return              an task object that matches to the given parameter
     */
    public static Task getNewTask(User user, int difficulty, boolean stay) {
        List<Task>          taskList    = null;
        if(stay) {
            taskList = user.getCurrentTaskSet().getTasks();
        } else {
            taskList = Task.find
                    .where()
                    .eq("points", difficulty)
                    .eq("available", true)
                    .eq("taskSet.available", true)
                    .eq("taskSet.isHomework", false)
                    .findList();
        }
        List<SolvedTask>    solvedTasks = SolvedTask.find
                .fetch("task")
                .where()
                .eq("profile", user)
                .in("task", taskList)
                .findList();

        if((taskList.size() == 0)) {
            if(difficulty > 1)
                return getNewTask(user, difficulty -1, stay);
            else
                return null;
        }

        if(taskList.size() > solvedTasks.size()) {
            for(SolvedTask solvedTask : solvedTasks) {
                taskList.remove(solvedTask.getTask());
            }
        } else {
            int avg = 0;
            for(SolvedTask solvedTask : solvedTasks) {
                avg += solvedTask.getTrys();
            }
            avg /= solvedTasks.size();
            for(SolvedTask solvedTask : solvedTasks) {
                if(solvedTask.getTrys() > avg) {
                    taskList.remove(solvedTask.getTask());
                }
            }
        }

        if((taskList.size() == 0)) {
            if(difficulty > 1)
                return getNewTask(user, difficulty -1, stay);
            else
                return null;
        }

        return taskList.get(Random.randomInt(taskList.size() -1 ));
    }

      /**
       *
       *
       * @return    returns a list of all task objects
       */
      public static List<Task> getAll() {
          return Task.find.all();
      }
}
