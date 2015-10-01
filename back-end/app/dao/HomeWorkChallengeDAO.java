package dao;

import models.HomeWorkChallenge;
import models.Profile;
import models.SubTask;
import models.TaskFile;

import play.Logger;

import javax.persistence.PersistenceException;

import java.util.Date;
import java.util.List;


public class HomeWorkChallengeDAO {
  /**
   *  New HomeworkChallenge
   * @param name
   * @param creator
   * @param solve_type
   * @param solve_type_extension
   * @param taskFiles
   * @param type
   * @param start_at
   * @param expires_at
   * @return
   */
  public static HomeWorkChallenge create(
          String name,
          Profile creator,
          int solve_type,
          int solve_type_extension,
          List<TaskFile> taskFiles,
          int type,
          Date start_at,
          Date expires_at) {

    if (taskFiles == null || taskFiles.size() == 0) {
      throw new IllegalArgumentException();
    }

    if (HomeWorkChallenge.find.where().between("expires_at", start_at, expires_at).findList().size() > 0 ||
        HomeWorkChallenge.find.where().between("start_at", start_at, expires_at).findList().size() > 0 ||
        HomeWorkChallenge.find.where().lt("start_at", start_at).gt("expires_at", expires_at).findList().size() > 0 ||
        HomeWorkChallenge.find.where().gt("start_at", start_at).lt("expires_at", expires_at).findList().size() > 0) {
      Logger.info("There is already a HomeWork during these Times!");
      return null;
    }

    HomeWorkChallenge homeWorkChallenge = new HomeWorkChallenge(
        name,
        creator,
        solve_type,
        solve_type_extension,
        taskFiles,
        type,
        start_at,
        expires_at);

    try {
      homeWorkChallenge.save();
    } catch (PersistenceException pe) {

    }
    return homeWorkChallenge;
  }

  /**
   *  Get all HomeWorks
   * @return
   */
  public static List<HomeWorkChallenge> getAll() {
    List<HomeWorkChallenge> homeWorkList = HomeWorkChallenge.find.all();

    if (homeWorkList == null) {
      Logger.warn("HomeWorkChallenge.getAll - no Elements found");
      return null;
    }

    return homeWorkList;
  }

  public static List<HomeWorkChallenge> getHomeWorksForSubmits(List<Object> submits) {
    return HomeWorkChallenge.find.where().in("id", submits).findList();
  }

  /**
   *  Get Instance by Id
   * @param id
   * @return
   */
  public static HomeWorkChallenge getById (long id) {
    return HomeWorkChallenge.find.byId(id);
  }

  public static HomeWorkChallenge getCurrent() {
    HomeWorkChallenge currentChallenge = HomeWorkChallenge.find.where().lt("start_at", new Date()).gt("expires_at", new Date()).findUnique();

    return currentChallenge;
  }
}
