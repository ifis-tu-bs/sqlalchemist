package dao;

import models.HomeWork;
import models.TaskSet;

import models.User;
import play.Logger;

import javax.persistence.PersistenceException;

import java.util.Date;
import java.util.List;


public class HomeWorkDAO {

    public static HomeWork create(
            String name,
            User creator,
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

    public static List<HomeWork> getAllStudent() {
        List<HomeWork> homeWorkList = HomeWork.find.where().lt("start_at", new Date()).findList();

        if (homeWorkList == null) {
            Logger.warn("HomeWorkChallenge.getAllStudent - no Elements found");
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


}
