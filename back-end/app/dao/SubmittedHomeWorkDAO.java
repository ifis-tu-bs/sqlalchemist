package dao;


import models.HomeWorkChallenge;
import models.Profile;
import models.SubmittedHomeWork;
import models.SubTask;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class SubmittedHomeWorkDAO  {

  //////////////////////////////////////////////////
  //  Getter Object
  //////////////////////////////////////////////////

      public static List<SubmittedHomeWork> getSubmitsForSubtask(SubTask subTask) {
          return SubmittedHomeWork.find.where().eq("sub_task_id", subTask.getId()).findList();
      }

      public static List<Object> getSubmitsForProfile(Profile profile) {
          return SubmittedHomeWork.find.where().eq("profile_id", profile.getId()).findIds();
      }

      public static List<SubmittedHomeWork> getSubmitsForSubtaskAndHomeWorkChallenge(long subTaskId, long homeWorkChallengeId) {
          return SubmittedHomeWork.find.where().eq("sub_task_id", subTaskId).eq("home_work_id", homeWorkChallengeId).findList();
      }

  //////////////////////////////////////////////////
  //  Submit Method
  //////////////////////////////////////////////////


      public static SubmittedHomeWork submit(
              Profile profile,
              SubTask subTask,
              boolean solve,
              String statement) {

          if (HomeWorkChallengeDAO.getCurrent() == null) {
              Logger.warn("Trying to submit without having an active HomeWork!!!");
              return null;
          }

          if (!HomeWorkChallengeDAO.getCurrent().contains(subTask)) {
              Logger.info("SubmittedHomeWork.submit - SomeOne got Late");
              return null;
          }

          SubmittedHomeWork existed = getCurrentSubmittedHomeWorkForProfileAndSubTask(profile, subTask);


          if (existed != null) {
              try {
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
                  subTask,
                  HomeWorkChallengeDAO.getCurrent(),
                  solve,
                  statement);

          try {
              submittedHomeWork.save();

              return submittedHomeWork;
          } catch (PersistenceException pe) {
              Logger.warn("Not saving: " + pe.getMessage());
              return null;
          }
      }


    /**
     * Returns current the Submit for given Subtask and Profile HomeWork
     * (Only on the Current HomeWork: See HomeWorkChallengeDAO.getCurrent())
     * @param profile Profile of the Submitter
     * @param subTask subTask being submitted
     * @return The given Object, or null if none exists
     */
    public static SubmittedHomeWork getCurrentSubmittedHomeWorkForProfileAndSubTask(
            Profile profile,
            SubTask subTask) {

        HomeWorkChallenge currentHomeWorkChallenge;
        if ((currentHomeWorkChallenge = HomeWorkChallengeDAO.getCurrent()) == null ) {
            return null;
        }

        return SubmittedHomeWork.find.where()
                .eq("profile_id", profile.getId())
                .eq("sub_task_id", subTask.getId())
                .eq("home_work_id", currentHomeWorkChallenge.getId())
                .findUnique();
    }
}
