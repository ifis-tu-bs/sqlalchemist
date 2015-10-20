package dao;

import models.HomeWork;
import models.Profile;
import models.TaskSet;

import play.Logger;

import javax.persistence.PersistenceException;

import java.util.Date;
import java.util.List;


public class HomeWorkDAO {

  public static HomeWork create(
          String name,
          Profile creator,
          List<TaskSet> taskSets,
          Date start_at,
          Date expire_at) {

    if (taskSets == null || taskSets.size() == 0) {
      throw new IllegalArgumentException();
    }

    HomeWork homeWork = new HomeWork(
        name,
        creator,
        taskSets,
        start_at,
        expire_at);

    try {
      homeWork.save();
    } catch (PersistenceException pe) {
      Logger.info("HomeWorkDAO.java - create: could not save homeWork: " + homeWork.getHomeWorkName());
      return null;
    }
    return homeWork;
  }

  public static List<HomeWork> getAll() {
    List<HomeWork> homeWorkList = HomeWork.find.all();

    if (homeWorkList == null) {
      Logger.warn("HomeWorkChallenge.getAll - no Elements found");
      return null;
    }

    return homeWorkList;
  }

  public static List<HomeWork> getHomeWorksForSubmits(List<Object> submits) {
    return HomeWork.find.where().in("id", submits).findList();
  }

  public static HomeWork getById (long id) {
    return HomeWork.find.byId(id);
  }

  public static HomeWork getCurrent() {

      return HomeWork.find.where().lt("start_at", new Date()).gt("expire_at", new Date()).findUnique();
  }


}
