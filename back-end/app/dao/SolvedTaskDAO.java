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

  public static SolvedTask getByUserAndTask(User user, List<Task> task) {
      if(user == null || task == null) {
          Logger.warn("SolvedTask.getByProfileAndSubTask - profile or subTask is null");
          return null;
      }
      try {
          SolvedTask solvedTask = find.where().eq("user", user).("task", task).findList();
          if(solvedTask == null) {
              Logger.warn("SolvedTask.getByProfileAndSubTask - Can't find existing solvedTask object!");
              return null;
          }
          return solvedTask;
      } catch (NullPointerException e) {
          Logger.warn("SolvedTask.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
          return null;
      }
  }
}
