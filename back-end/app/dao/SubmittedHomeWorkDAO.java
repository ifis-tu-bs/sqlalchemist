package dao;


import models.HomeWork;
import models.Profile;
import models.SubmittedHomeWork;
import models.Task;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class SubmittedHomeWorkDAO  {

  //////////////////////////////////////////////////
  //  Getter Object
  //////////////////////////////////////////////////

      public static List<SubmittedHomeWork> getSubmitsForSubtask(Task task) {
          return SubmittedHomeWork.find.where().eq("task_id", task.getId()).findList();
      }

      public static List<Object> getSubmitsForProfile(Profile profile) {
          return SubmittedHomeWork.find.where().eq("profile_id", profile.getId()).findIds();
      }

      public static List<SubmittedHomeWork> getSubmitsForTaskInHomeWork(long taskId, long homeWorkId) {
          return SubmittedHomeWork.find.where().eq("task_id", taskId).eq("home_work_id", homeWorkId).findList();
      }

  //////////////////////////////////////////////////
  //  Special Getter Object
  //////////////////////////////////////////////////

    /**
     * Returns current the Submit for given Subtask and Profile HomeWork
     * (Only on the Current HomeWork: See HomeWorkChallengeDAO.getCurrent())
     * @param profile Profile of the Submitter
     * @param task task being submitted
     * @return The given Object, or null if none exists
     */
    public static SubmittedHomeWork getCurrentSubmittedHomeWorkForProfileAndTask(
            Profile profile,
            Task task) {

        HomeWork currentHomeWork;
        if ((currentHomeWork = HomeWorkDAO.getCurrent()) == null ) {
            return null;
        }

        return SubmittedHomeWork.find.where()
                .eq("profile_id", profile.getId())
                .eq("task_id", task.getId())
                .eq("home_work_id", currentHomeWork.getId())
                .findUnique();
    }

  //////////////////////////////////////////////////
  //  Submit Method
  //////////////////////////////////////////////////


      public static SubmittedHomeWork submit(
              Profile profile,
              Task task,
              boolean solve,
              String statement) {

          if (HomeWorkDAO.getCurrent() == null) {
              Logger.warn("Trying to submit without having an active HomeWork!!!");
              return null;
          }

          if (!HomeWorkDAO.getCurrent().contains(task)) {
              Logger.info("SubmittedHomeWork.submit - SomeOne got Late");
              return null;
          }

          SubmittedHomeWork existed = getCurrentSubmittedHomeWorkForProfileAndTask(profile, task);


          if (existed != null) {
              try {
                  existed.addSemanticCheck();
                  existed.setSolve(solve);
                  existed.setStatement(statement);

                  existed.update();
                  return existed;
              } catch (PersistenceException pe) {
                  Logger.warn("Cannot Submit: " + pe.getMessage());
              }
          }


          SubmittedHomeWork submittedHomeWork = new SubmittedHomeWork(
                  profile,
                  task,
                  HomeWorkDAO.getCurrent(),
                  solve,
                  statement);

          try {
              submittedHomeWork.addSemanticCheck();
              submittedHomeWork.save();

              return submittedHomeWork;
          } catch (PersistenceException pe) {
              Logger.warn("Not saving: " + pe.getMessage());
              return null;
          }
      }


}
