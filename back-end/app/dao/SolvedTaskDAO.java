package dao;

import com.avaje.ebean.Model;
import models.SolvedTask;
import models.Task;
import models.User;

import play.Logger;

import java.util.List;

public class SolvedTaskDAO {
  private static final Model.Finder<Long, SolvedTask> find = new Model.Finder<>(SolvedTask.class);

  public static SolvedTask create(
      User user,
      Task task,
      boolean solved,
      int     timeSpend) {
      SolvedTask solvedTask =
          new SolvedTask(user, task, solved, timeSpend);

      solvedTask.save();

      return solvedTask;
  }

  public static List<SolvedTask> getByUserAndTask(User user, Task task) {
    if(user == null || task == null) {
      Logger.warn("SolvedTask.getByProfileAndSubTask - profile or subTask is null");
      return null;
    }
    try {
      List<SolvedTask> solvedTasks = find.where().eq("user", user).eq("task", task).findList();
      if(solvedTasks == null ||solvedTasks.isEmpty()) {
        Logger.warn("SolvedTask.getByProfileAndSubTask - Can't find existing solvedTask object!");
        return null;
      }
      return solvedTasks;
    } catch (NullPointerException e) {
      Logger.warn("SolvedTask.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
      return null;
    }
  }

  public static List<SolvedTask> getByUserAndTask(User user, List<Task> tasks) {
      if(user == null || tasks == null || tasks.isEmpty()) {
          Logger.warn("SolvedTask.getByProfileAndSubTask - profile or subTask is null");
          return null;
      }
      try {
          List<SolvedTask> solvedTasks = find.where().eq("user", user).in("task", tasks).findList();
          if(solvedTasks == null ||solvedTasks.isEmpty()) {
              Logger.warn("SolvedTask.getByProfileAndSubTask - Can't find existing solvedTask object!");
              return null;
          }
          return solvedTasks;
      } catch (NullPointerException e) {
          Logger.warn("SolvedTask.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
          return null;
      }
  }
}
