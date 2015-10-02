package dao;

import models.Profile;
import models.SolvedSubTask;
import models.SubTask;

import play.Logger;

import java.util.List;
import java.util.Date;

public class SolvedSubTaskDAO {

  private static SolvedSubTask create(Profile profile, SubTask subTask) {
      SolvedSubTask solvedSubTask = new SolvedSubTask(profile, subTask);

      solvedSubTask.save();

      return solvedSubTask;
  }

  public static void update(
          Profile profile,
          SubTask subTask,
          boolean solved ) {
      SolvedSubTask solvedSubTask = getByProfileAndSubTask(profile, subTask);
      if (solvedSubTask == null) {
          solvedSubTask = create(profile, subTask);
      }

      solvedSubTask.pushTrys();

      if(solved) {
          solvedSubTask.pushSolved();
          solvedSubTask.setLastSolved(new Date());
      }
      solvedSubTask.update();
  }

  private static SolvedSubTask getByProfileAndSubTask(Profile profile, SubTask subTask) {
      if(profile == null || subTask == null) {
          Logger.warn("SolvedSubTask.getByProfileAndSubTask - profile or subTask is null");
          return null;
      }

      try {
          SolvedSubTask solvedSubTask = SolvedSubTask.find.where().eq("profile", profile).eq("subTask", subTask).findUnique();


          if(solvedSubTask == null) {
              Logger.warn("SolvedSubTask.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
              return null;
          }

          return solvedSubTask;
      } catch (NullPointerException e) {
          Logger.warn("SolvedSubTask.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
          return null;
      }
  }

  public static List<SolvedSubTask> getAllDoneSubTask(Profile profile) {
      try {
          float mittelwert_try = 0;
          int i = 0;

          List<SolvedSubTask> solvedSubTaskList = SolvedSubTask.find.where().eq("profile", profile).findList();

          for(SolvedSubTask solvedSubTask : solvedSubTaskList) {
              if(i == 0) {
                  mittelwert_try = solvedSubTask.getTrys();
              } else {
                  mittelwert_try = (mittelwert_try + solvedSubTask.getTrys()) / 2;
              }

              i++;
          }

          List<SolvedSubTask> solvedSubTasks = SolvedSubTask.find.where().eq("profile", profile).ge("trys", mittelwert_try).findList();
          if(solvedSubTasks == null || solvedSubTaskList.size() == 0) {
              Logger.warn("SolvedSubTask.getAllDoneSubTask - no SubTaskCandidates found");
              return null;
          }

          return solvedSubTasks;
      } catch (NullPointerException e) {
          Logger.warn("SolvedSubTask.getAllDoneSubTask - no SubTaskCandidates found");
          return null;
      }
  }

}
