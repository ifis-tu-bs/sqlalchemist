package dao;

import models.SolvedTask;
import models.Task;
import models.User;

import play.Logger;

import java.util.List;
import java.util.Date;

public class SolvedTaskDAO {

  private static SolvedTask create(User user, Task task) {
      SolvedTask solvedTask = new SolvedTask(user, task);

      solvedTask.save();

      return solvedTask;
  }

  public static void update(
          User user,
          Task task,
          boolean solved ) {

      SolvedTask solvedTask = getByProfileAndTask(user, task);
      if (solvedTask == null) {
          solvedTask = create(user, task);
      }

      solvedTask.pushTrys();

      if(solved) {
          solvedTask.pushSolved();
          solvedTask.setLastSolved(new Date());
      }
      solvedTask.update();
  }

  public static SolvedTask getByProfileAndTask(User user, Task task) {
      if(user == null || task == null) {
          Logger.warn("SolvedTask.getByProfileAndSubTask - profile or subTask is null");
          return null;
      }
      try {
          SolvedTask solvedTask = SolvedTask.find.where().eq("user", user).eq("task", task).findUnique();
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

  public static List<SolvedTask> getAllDoneTask(User user) {
      try {
          float mittelwert_try = 0;
          int i = 0;

          List<SolvedTask> solvedTaskList = SolvedTask.find.where().eq("user", user).findList();

          for(SolvedTask solvedTask : solvedTaskList) {
              if(i == 0) {
                  mittelwert_try = solvedTask.getTrys();
              } else {
                  mittelwert_try = (mittelwert_try + solvedTask.getTrys()) / 2;
              }

              i++;
          }

          List<SolvedTask> solvedTasks = SolvedTask.find.where().eq("user", user).ge("trys", mittelwert_try).findList();
          if(solvedTasks == null || solvedTaskList.size() == 0) {
              Logger.warn("SolvedTask.getAllDoneSubTask - no SubTaskCandidates found");
              return null;
          }

          return solvedTasks;
      } catch (NullPointerException e) {
          Logger.warn("SolvedTask.getAllDoneSubTask - no SubTaskCandidates found");
          return null;
      }
  }

}
