package dao;

import Exception.SQLAlchemistException;

import helper.RATable;

import models.Profile;
import models.SubTask;
import models.TaskFile;

import sqlgame.sandbox.InputFile;
import sqlgame.sandbox.Task;
import sqlgame.xmlparse.Exercise;
import sqlgame.xmlparse.Relation;
import sqlgame.exception.MySQLAlchemistException;

import play.Logger;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.PersistenceException;

public class TaskFileDAO {
  /**
   * This method creates a TaskFile and the SubTask from a string that contains a valid xml file
   *
   * @param creator       creator of the TaskFile as Profile object
   * @param xmlContent    content of the xml File as String
   * @return              returns the created TaskFile object
   */
  public static TaskFile create(
          Profile creator,
          String  xmlContent,
          boolean isHomeWork
  )
          throws SQLAlchemistException
  {
      // Create XMLFile via MySQLAlchemist
      InputFile inputFile;
      Task task;
      try
      {
          inputFile = new InputFile(xmlContent, false);
          ArrayList<Task> tasks = inputFile.getTasks();
          if(tasks.size() == 1)
          {
              task = tasks.get(0);
          }
          else
          {
              Logger.warn("TaskFile.create - No Tasks in the TaskFile");
              throw new SQLAlchemistException("You have created no Tasks in the your TaskFile");
          }
      }
      catch (MySQLAlchemistException e)
      {
          Logger.error("TaskFile.create - catches MySQLAlchemistException: " + e.getMyMessage());
          throw new SQLAlchemistException(e.getMyMessage());
      } catch (Exception e) {
          Logger.error("TaskFile.create - catches MySQLAlchemistException: " + e.getMessage());
          throw new SQLAlchemistException(e.getMessage());
      }

      // Get metadata from MySQLAlchemist
      String fileName = task.getMyHeader().get(0).getTaskId() +  ".xml";
      String relations = "";
      for(Relation relation : task.getMyRelation())
      {
          String intension = (new RATable(relation.getIntension())).toString();


          relations += "\n" + intension;
      }
      String schema = relations;

      // Create TaskFile object and save it
      TaskFile taskFile = new TaskFile(creator, fileName, schema, xmlContent, isHomeWork);
      try
      {
          taskFile.save();
      }
      catch (PersistenceException pe)
      {
          Logger.warn("TaskFile.create - catches PersistenceException at saving TaskFile");
          TaskFile taskFile_comp = getByFileName(fileName);
          if (taskFile_comp != null)
          {
              throw new SQLAlchemistException("A TaskFile with this name is already in our database");
          }
      }

      // Create SubTask objects and save them
      List<SubTask> subTaskList = new ArrayList<>();
      for(Exercise exercise : task.getMyExercise())
      {
          int points = exercise.getPoints();
          String refStatement = exercise.getReferencestatement();

          String taskTexts = "";
          for(String taskText : exercise.getTasktexts())
          {
              taskTexts = taskTexts + taskText;
          }
          int index = exercise.getSubTaskId();

          SubTask subTask = new SubTask(
                  creator,
                  taskFile,
                  index,
                  points,
                  taskTexts,
                  refStatement,
                  isHomeWork);

          subTask.setHomeWork();
          try
          {
              subTask.save();
              subTaskList.add(subTask);
          }
          catch (PersistenceException pe)
          {
              Logger.warn("TaskFile.create - catches PersistenceException at saving SubTask");
              SubTask subTask_comp = SubTaskDAO.getByIndexOfTaskFile(taskFile, index);
              if(subTask_comp == null)
              {
                  Logger.warn("TaskFile.create - cannot save SubTask, exception: " + pe.getMessage());
                  for(SubTask subTaskI : subTaskList)
                  {
                      subTaskI.delete();
                  }

              }
              if(subTaskList.size() > 0) {
                  for (SubTask subTask1 : subTaskList) {
                      subTask1.delete();
                  }
              }
              taskFile.delete();
              throw new SQLAlchemistException("cannot save SubTasks of TaskFile, please set SubTaskIds");
          }
      }
      taskFile.addSubTask(subTaskList);
      taskFile.update();


      taskFile.makeAvailable();
      taskFile.update();

      return taskFile;
  }

  //////////////////////////////////////////////////
  //  object find methods
  //////////////////////////////////////////////////

      /**
       * get a list of all TaskFile objects
       * @return returns a list of all TaskFile objects
       */
      public static List<TaskFile> getAll()
      {
          List<TaskFile> taskFileList = TaskFile.find.all();

          if (taskFileList == null)
          {
              Logger.warn("TaskFile.getAll - no TaskFile Objects-Found");
              return null;
          }
          return taskFileList;
      }

      /**
       *
       *
       * @param fileName  require a fileName as String
       * @return          returns a TaskFile object with the given file name or null
       */
      public static TaskFile getByFileName(String fileName)
      {
          TaskFile taskFile = TaskFile.find.where().eq("fileName", fileName).findUnique();

          if (taskFile == null)
          {
              Logger.warn("TaskFile.getByFileName - cannot find TaskFile with fileName: " + fileName);
              return null;
          }
          return taskFile;
      }

      /**
       * This method finds the TaskFile with the given id
       *
       * @param id    the id of a TaskFile as Long
       * @return      returns the matching TaskFile or null
       */
      public static TaskFile getById(Long id)
      {
          TaskFile taskFile = TaskFile.find.byId(id);

          if (taskFile == null)
          {
              Logger.warn("TaskFile.getById("+id+") - no TaskFile found");
              return null;
          }
          return taskFile;
      }


}
