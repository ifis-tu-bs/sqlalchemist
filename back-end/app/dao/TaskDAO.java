package dao;

import helper.Random;

import models.SolvedTask;
import models.Task;
import models.User;

import java.util.ArrayList;
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
     * @param user       the user
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

        if((taskList.size() == 0)) {
            if(difficulty > 1)
                return getNewTask(user, difficulty -1, stay);
            else
                return null;
        }

      int avg = 0;
      int size = 0;
      for(Task task : taskList) {
        List<SolvedTask>    solvedTasks = SolvedTaskDAO.getByUserAndTask(user, task);
        avg += (solvedTasks == null) ? 0 : solvedTasks.size();
        size += (solvedTasks == null) ? 0 : solvedTasks.size();
      }

      if(size != 0) {
        avg /= size;
      }

      List<Task> tasksToRemove = new ArrayList<>();
      for(Task task : taskList) {
        List<SolvedTask>    solvedTasks = SolvedTaskDAO.getByUserAndTask(user, task);
        if(((solvedTasks == null) ? 0 : solvedTasks.size()) > avg) {
          tasksToRemove.add(task);
        }
      }
      taskList.remove(tasksToRemove);

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
