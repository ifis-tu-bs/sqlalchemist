package dao;

import helper.Random;

import models.Profile;
import models.Scroll;
import models.ScrollCollection;
import models.SolvedSubTask;
import models.SubTask;
import models.TaskFile;

import play.Logger;

import java.util.List;

public class SubTaskDAO {
  //////////////////////////////////////////////////
  //  Object Getter Methods
  //////////////////////////////////////////////////

      /**
       *
       * @param id
       * @return
       */
      public static SubTask getById(long id) {
          return SubTask.find.byId(id);
      }

      /**
       *
       * @param potionID
       * @return
       */
      public static SubTask getByPotionID(long potionID, Profile profile) {
          List<SubTask> list = SubTask.find.where().eq("potion_id", potionID).findList();

          SubTask subTask = list.get(0);

          return subTask;
      }

      /**
       *
       * @param profile
       * @param scroll
       * @return
       */
      public static SubTask getByScroll(Profile profile, Scroll scroll) {
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

      public static SubTask getByDifficulty(Profile profile, int points) {
          List<SubTask> subTaskList           = SubTask.find.where().eq("available", true).eq("points", points).findList(); //.eq("is_home_work", false).findList();
          List<SolvedSubTask> solvedSubTasks  = SolvedSubTaskDAO.getAllDoneSubTask(profile);

          if(subTaskList == null || subTaskList.size() == 0) {
              Logger.warn("SubTask.getByDifficulty - No isAvailable SubTasks found");
              return null;
          }

          if(solvedSubTasks == null || solvedSubTasks.size() == 0) {
              return subTaskList.get(Random.randomInt(0, subTaskList.size() - 1));
          }
          for(SolvedSubTask solvedSubTask : solvedSubTasks) {
              if(subTaskList.contains(solvedSubTask.getSubTask())) {
                  subTaskList.remove(solvedSubTask.getSubTask());
              }
          }
          if(subTaskList.size() == 0) {
              subTaskList           = SubTask.find.where().eq("available", true).eq("points", points).findList();
          }

          int i = Random.randomInt(0, subTaskList.size() - 1);

          return subTaskList.get(i);
      }


      /**
       *
       * @param challengeID
       * @return
       */
      public static SubTask getByChallengeID(long challengeID, Profile profile) {

          return null;
      }

      /**
       * ToDO
       * @param taskFile
       * @param index
       * @return
       */
      public static SubTask getByIndexOfTaskFile(TaskFile taskFile, int index) {
          SubTask subTask = SubTask.find.where().eq("taskFile", taskFile).eq("index", index).findUnique();
          if (subTask == null) {
              Logger.warn("SubTask.getByIndexOfTaskFile - cannot find SubTask with Index: " + index + " and taskFile: " + taskFile);
              return null;
          }
          return subTask;
      }

      /**
       *
       *
       * @return
       */
      public static List<SubTask> getAll() {
          List<SubTask> subTaskList = SubTask.find.all();
          if (subTaskList == null) {
              Logger.warn("SubTask.getAll - no entitys saved");
              return null;
          }
          return subTaskList;
      }
}
