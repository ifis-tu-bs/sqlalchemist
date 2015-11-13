package dao;

import models.Profile;
import models.SolvedTask;
import models.Task;

import play.Logger;

import java.util.List;
import java.util.Date;

public class SolvedTaskDAO {

  private static SolvedTask create(Profile profile, Task task) {
      SolvedTask solvedTask = new SolvedTask(profile, task);

      solvedTask.save();

      Logger.info("Saving SolvedTask: ProfileID: " + profile.getId() + ", taskID: " + task.getId() );

      return solvedTask;
  }

  public static void update(
          Profile profile,
          Task task,
          boolean solved ) {

      SolvedTask solvedTask = getByProfileAndTask(profile, task);
      if (solvedTask == null) {
          solvedTask = create(profile, task);
      }

      solvedTask.pushTrys();

      if(solved) {
          solvedTask.pushSolved();
          solvedTask.setLastSolved(new Date());
      }
      solvedTask.update();
  }

  public static SolvedTask getByProfileAndTask(Profile profile, Task task) {
      if(profile == null || task == null) {
          Logger.warn("SolvedTaskDAO.getByProfileAndSubTask - profile or subTask is null");
          return null;
      }

      Logger.info("try getting SolvedTask for: ProfileID: " + profile.getId() + ", taskID: " + task.getId() );

      try {
          SolvedTask solvedTask = SolvedTask.find.where().eq("profile_id", profile.getId()).eq("task_id", task.getId()).findUnique();
          if(solvedTask == null) {
              Logger.warn("SolvedTaskDAO.getByProfileAndSubTask - Can't find existing solvedTask object!");
              return null;
          }
          return solvedTask;
      } catch (NullPointerException e) {
          Logger.warn("SolvedTaskDAO.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
          return null;
      }
  }

  public static List<SolvedTask> getAllDoneTask(Profile profile) {
      try {
          float mittelwert_try = 0;
          int i = 0;

          List<SolvedTask> solvedTaskList = SolvedTask.find.where().eq("profile_id", profile).findList();

          for(SolvedTask solvedTask : solvedTaskList) {
              if(i == 0) {
                  mittelwert_try = solvedTask.getTrys();
              } else {
                  mittelwert_try = (mittelwert_try + solvedTask.getTrys()) / 2;
              }

              i++;
          }

          List<SolvedTask> solvedTasks = SolvedTask.find.where().eq("profile_id", profile.getId()).ge("trys", mittelwert_try).findList();
          if(solvedTasks == null || solvedTaskList.size() == 0) {
              Logger.warn("SolvedTaskDAO.getAllDoneSubTask - no SubTaskCandidates found");
              return null;
          }

          return solvedTasks;
      } catch (NullPointerException e) {
          Logger.warn("SolvedTaskDAO.getAllDoneSubTask - no SubTaskCandidates found");
          return null;
      }
  }

}
