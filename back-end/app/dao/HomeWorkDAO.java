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

    if (HomeWork.find.where().between("expire_at", start_at, expire_at).findList().size() > 0 ||
        HomeWork.find.where().between("start_at", start_at, expire_at).findList().size() > 0 ||
        HomeWork.find.where().lt("start_at", start_at).gt("expire_at", expire_at).findList().size() > 0 ||
        HomeWork.find.where().gt("start_at", start_at).lt("expire_at", expire_at).findList().size() > 0) {
      Logger.info("There is already a HomeWork during these Times!");
      return null;
    }

    HomeWork homeWork = new HomeWork(
        name,
        creator,
        taskSets,
        start_at,
        expire_at);

    try {
      homeWork.save();
    } catch (PersistenceException ignored) {

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
    HomeWork currentChallenge = HomeWork.find.where().lt("start_at", new Date()).gt("expire_at", new Date()).findUnique();

    return currentChallenge;
  }


}
