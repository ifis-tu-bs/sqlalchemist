package dao;


import models.HomeWork;
import models.Profile;
import models.SubmittedHomeWork;
import models.Task;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class SubmittedHomeWorkDAO  {
    public static SubmittedHomeWork create(Profile profile, Task task, HomeWork homeWork) {
        SubmittedHomeWork submittedHomeWork = new SubmittedHomeWork(
                profile,
                task,
                homeWork);

        try {
            submittedHomeWork.save();
        } catch (PersistenceException pe) {
            Logger.warn("Not saving: " + pe.getMessage());
            return null;
        }
        return submittedHomeWork;
    }
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

    public static SubmittedHomeWork getSubmitsForProfileHomeWorkTask(Profile profile, HomeWork homeWork, Task task) {
        return SubmittedHomeWork.find.where().eq("profile", profile).eq("homework", homeWork).eq("task", task).findUnique();
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
    public static SubmittedHomeWork getCurrentSubmittedHomeWorkForProfileAndTaskAndHomeWork(
            Profile profile,
            Task task,
            HomeWork homeWork) {

        return SubmittedHomeWork.find.where()
                .eq("profile_id", profile.getId())
                .eq("task_id", task.getId())
                .eq("home_work_id", homeWork.getId())
                .findUnique();
    }
}
