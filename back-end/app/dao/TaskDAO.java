package dao;

import helper.Random;

import models.Profile;
import models.Scroll;
import models.ScrollCollection;
import models.SolvedTask;
import models.Task;

import play.Logger;

import java.util.List;

public class TaskDAO {
  //////////////////////////////////////////////////
  //  Object Getter Methods
  //////////////////////////////////////////////////

      /**
       *
       * @param id
       * @return
       */
      public static Task getById(long id) {
          return Task.find.byId(id);
      }

      /**
       *
       * @param potionID
       * @return
       */
      public static Task getByPotionID(long potionID, Profile profile) {
          List<Task> list = Task.find.where().eq("potion_id", potionID).findList();

          return list.get(0);
      }

      /**
       *
       * @param profile
       * @param scroll
       * @return
       */
      public static Task getByScroll(Profile profile, Scroll scroll) {
          float points = 0;

          if(scroll.isRecipe()) {
              points = scroll.getPotion().getPowerLevel();
          } else {
              List<ScrollCollection> scrollList = ScrollCollectionDAO.getScrollCollection(profile);
              for(ScrollCollection scrollCollection : scrollList) {
                  Scroll singleScroll = scrollCollection.getScroll();
                  if( !singleScroll.isRecipe() && singleScroll.getType() == scroll.getType()) {
                      points++;
                  }
              }
          }
          Logger.info("Diffi: " + Math.round(points / 2));
          return getByDifficulty(profile, Math.round(points / 2));
      }

      public static Task getByDifficulty(Profile profile, int points) {
          List<Task> TaskList           = Task.find.where().eq("available", true).eq("points", points).findList(); //.eq("is_home_work", false).findList();
          List<SolvedTask> solvedTasks  = SolvedTaskDAO.getAllDoneTask(profile);

          if(TaskList == null || TaskList.size() == 0) {
              Logger.warn("Task.getByDifficulty - No isAvailable Tasks found");
              return null;
          }

          if(solvedTasks == null || solvedTasks.size() == 0) {
              return TaskList.get(Random.randomInt(0, TaskList.size() - 1));
          }
          for(SolvedTask solvedTask : solvedTasks) {
              if(TaskList.contains(solvedTask.getTask())) {
                  TaskList.remove(solvedTask.getTask());
              }
          }
          if(TaskList.size() == 0) {
              TaskList           = Task.find.where().eq("available", true).eq("points", points).findList();
          }

          int i = Random.randomInt(0, TaskList.size() - 1);

          return TaskList.get(i);
      }


      /**
       *
       * @param challengeID
       * @return
       */
      public static Task getByChallengeID(long challengeID, Profile profile) {

          return null;
      }

      /**
       *
       *
       * @return
       */
      public static List<Task> getAll() {
          List<Task> TaskList = Task.find.all();
          if (TaskList == null) {
              Logger.warn("Task.getAll - no entitys saved");
              return null;
          }
          return TaskList;
      }
}
