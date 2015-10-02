package models;

import dao.SubTaskDAO;

import Exception.SQLAlchemistException;

import helper.*;

import sqlgame.sandbox.*;
import sqlgame.xmlparse.*;
import sqlgame.exception.MySQLAlchemistException;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;
import play.Play;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * this class objects matches to the task files on disk drive
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "task_file")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class TaskFile extends Model {
    @Id
    private long id;

    @OneToOne
    private final Profile creator;

    private final boolean isHomeWork;

    private boolean isAvailable;

    @Column(unique = true)
    private final String fileName;

    @Column(name = "relation_schema", columnDefinition = "Text")
    private final String schema;

    @OneToMany(mappedBy = "taskFile")
    private List<SubTask> subTasks;

    @ManyToMany
    private List<Rating> ratings;

    @ManyToMany
    private List<Comment> commentList;

    @Column(name = "xmlContent", columnDefinition = "Text")
    private String xmlContent;

    private final Date created_at;

    private static final Finder<Long, TaskFile> find = new Finder<>(Long.class, TaskFile.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    /**
     * this is the constructor for TaskFile
     *
     * @param creator   the creator of this object as Profile object or null
     * @param fileName  the fileName of this object
     * @param schema    the schema of the taskFile
     */
    private TaskFile(
            Profile creator,
            String  fileName,
            String  schema,
            String  xmlContent,
            boolean isHomeWork)
    {
        super();

        this.xmlContent         = xmlContent;
        this.creator            = creator;
        this.isHomeWork         = isHomeWork;
        if(isHomeWork) {
            this.isAvailable = false;
        } else {
            this.isAvailable = Play.application().configuration().getBoolean("TaskFile.Default.status");
        }
        this.fileName           = fileName;
        this.schema             = schema;
        this.subTasks           = new ArrayList<>();
        this.ratings            = new ArrayList<>();
        this.commentList        = new ArrayList<>();
        this.created_at         = new Date();
    }

    @Override
    public void delete() {
        List<SubTask> subTasks = this.getSubTasks();
        if(subTasks != null && subTasks.size() > 0) {
            for(SubTask subTask : subTasks) {
                subTask.delete();
            }
        }
        super.delete();
    }


    //////////////////////////////////////////////////
//  getter & setter methods
//////////////////////////////////////////////////

    /**
     *
     *
     * @return returns the od pf
     */
    public long getId()
    {
        return this.id;
    }

    /**
     * getter method for attribute schema
     *
     * @return  returns the value of schema
     */
    public String getSchema()
    {
        if(this.schema == null) {
            Logger.warn("TaskFile.getSchema - schema is null!");
        }
        return this.schema;
    }

    /**
     *
     * @return the xmlCode of the Task
     */
    public String getXmlContent()
    {
        return this.xmlContent;
    }

    /**
     * getter method for task objects
     *
     * @return  return the task object from XML file
     */
    public Task getTask()
    {
        try {
            InputFile inputFile = new InputFile(this.fileName, true);

            return inputFile.getTasks().get(0);
        } catch (MySQLAlchemistException e) {
            Logger.warn("TaskFile.getTask - catches MySQLAlchemistError: " + e.getMyMessage());
        }
        return null;
    }

    public List<Comment> getCommentList() {
        return this.commentList;
    }

    public List<SubTask> getSubTasks() {
        return this.subTasks;
    }

    public boolean contains(SubTask subTask) {
        return subTasks.contains(subTask);
    }
//////////////////////////////////////////////////
//  json method
//////////////////////////////////////////////////

    /**
     * converts the TaskFile object to Json
     * @return returns the object as ObjectNode
     */
    public ObjectNode toJson()
    {
        ObjectNode  objectNode = Json.newObject();
        ArrayNode   subTasks   = JsonNodeFactory.instance.arrayNode();
        List<Rating> ratings   = new ArrayList <>(this.ratings);

        for(SubTask subTask : this.subTasks) {
            subTasks.add(subTask.toJson());
            ratings.addAll(subTask.getRatings());
        }

        Rating      rating_sum  = Rating.sum(ratings);

        objectNode.put("id", this.id);
        if(this.creator != null) {
            objectNode.put("creator", this.creator.toJsonProfile());
        } else {
            objectNode.put("creator", "");
        }
        objectNode.put("isHomeWork",    this.isHomeWork);
        objectNode.put("isAvailable",   this.isAvailable);
        objectNode.put("fileName",      this.fileName);
        objectNode.put("schema",        this.schema);
        objectNode.put("rating",        rating_sum.toJson());
        objectNode.put("created_at",    String.valueOf(this.created_at));
        objectNode.put("subTasks",      subTasks);

        return objectNode;
    }

    /**
     *
     * @return
     */
    public ObjectNode toFileNameJson() {
        ObjectNode objectNode = Json.newObject();

        objectNode.put("fileName", this.fileName);

        return objectNode;
    }

//////////////////////////////////////////////////
//  action
//////////////////////////////////////////////////

    /**
     * add subtasks of a List to own subtask list
     * @param subTaskList   a list filled with subtasks
     */
    private void addSubTask(List<SubTask> subTaskList)
    {
        for(SubTask subTask : subTaskList) {
            this.subTasks.add(subTask);
        }
    }

    /**
     * This is the methode to add a rating to this entity
     */
    public void addRating(Rating rating) {
      if(this.ratings != null && this.ratings.size() > 0) {
        for(Rating ratingI : this.ratings) {
          if(ratingI.getProfile().getId() == rating.getProfile().getId()) {
            ratingI.delete();
            break;
          }
        }
      } else {
        this.ratings = new ArrayList<>();
      }

      this.ratings.add(rating);
    }

    private void checkTaskFile() {

    }

    /**
     * this method adds comments to the TaskFile
     *
     * @param comment   the comment to be added
     */
    public void addComment(Comment comment)
    {
        this.commentList.add(comment);
    }
//////////////////////////////////////////////////
//  create methods
//////////////////////////////////////////////////

    public boolean submittedAll(List<Object> submits) {
        boolean answer = true;
        for (SubTask subTask : this.subTasks) {
            answer &= submits.contains(subTask.getId());
        }
        return answer;
    }

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
            TaskFile taskFile_comp = TaskFile.getByFileName(fileName);
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

    private void makeAvailable() {
        List<SubTask> subTaskList = this.getSubTasks();

        for (SubTask subTask : subTaskList) {
            subTask.makeAvailable();
            subTask.update();
        }

        this.setAvailable(true);
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
        List<TaskFile> taskFileList = find.all();

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
        TaskFile taskFile = find.where().eq("fileName", fileName).findUnique();

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
        TaskFile taskFile = find.byId(id);

        if (taskFile == null)
        {
            Logger.warn("TaskFile.getById("+id+") - no TaskFile found");
            return null;
        }
        return taskFile;
    }

//////////////////////////////////////////////////
//  init method
//////////////////////////////////////////////////

    /**
     * This method fill the database with initial data
     */
    public static void init()
    {
        Logger.info("TaskFile.init - Initialize data");


        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<tasks schemaversion=\"1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"recipe.xsd\">\n" +
                        "\n" +
                        "  <task taskid=\"exercises-wise13\" querylanguage=\"SQL\">\n" +
                        "    \n" +
                        "    <titles>\n" +
                        "      <title language=\"EN\">Super Heros</title>\n" +
                        "    </titles>\n" +
                        "    \n" +
                        "    <flufftexts>\n" +
                        "      <flufftext language=\"EN\">Super Heros und Super Villains.</flufftext>\n" +
                        "    </flufftexts>\n" +
                        "    \n" +
                        "    <schema>\n" +
                        "      <relation>\n" +
                        "        <intension>\n" +
                        "          CREATE TABLE Comic(\n" +
                        "            no INTEGER NOT NULL PRIMARY KEY,\n" +
                        "            title VARCHAR(255) NOT NULL,\n" +
                        "            pages INTEGER,\n" +
                        "            publisher VARCHAR(255)\n" +
                        "          );\n" +
                        "        </intension>\n" +
                        "        <extension>\n" +
                        "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (12, 'Wonderboy''s Comeback', 'Marvel');</tuple>\n" +
                        "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (13, 'Superman Returns', 'DC');</tuple>\n" +
                        "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (15, 'The Revenge of Young Nastyman', 'Marvel');</tuple>\n" +
                        "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (18, 'Wonderboy Rocks', 'Marvel');</tuple>\n" +
                        "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (21, 'Young Nastyman''s Revenge', 'Marvel');</tuple>\n" +
                        "        </extension>\n" +
                        "      </relation>\n" +
                        "      <relation>\n" +
                        "        <intension>\n" +
                        "          CREATE TABLE PoweredPerson(\n" +
                        "            alias VARCHAR(255) NOT NULL PRIMARY KEY,\n" +
                        "            firstname VARCHAR(100) NOT NULL,\n" +
                        "            lastname VARCHAR(100),\n" +
                        "            evilness_level INTEGER\n" +
                        "          );\n" +
                        "        </intension>\n" +
                        "        <extension>\n" +
                        "          <tuple>INSERT INTO PoweredPerson VALUES ('Wonderboy', 'KG', NULL, -25);</tuple>\n" +
                        "          <tuple>INSERT INTO PoweredPerson VALUES ('Magneto', 'Erik Magnus', 'Lensherr', 46);</tuple>\n" +
                        "          <tuple>INSERT INTO PoweredPerson VALUES ('Superman', 'Clark', 'Kent', -63);</tuple>\n" +
                        "          <tuple>INSERT INTO PoweredPerson VALUES ('Professor X', 'Charles', 'Xavier', -41);</tuple>\n" +
                        "          <tuple>INSERT INTO PoweredPerson VALUES ('Young Nastyman', 'JB', NULL, 39);</tuple>\n" +
                        "          <tuple>INSERT INTO PoweredPerson VALUES ('Mystique', 'Raven', 'Darkholme', 43);</tuple>\n" +
                        "        </extension>\n" +
                        "      </relation>\n" +
                        "      <relation>\n" +
                        "        <intension>\n" +
                        "          CREATE TABLE HasPowerSince (\n" +
                        "            comic INTEGER NOT NULL REFERENCES Comic(no),\n" +
                        "            alias VARCHAR(255) NOT NULL REFERENCES PoweredPerson (alias),\n" +
                        "            power VARCHAR(100) NOT NULL,\n" +
                        "            PRIMARY KEY (comic, alias, power)\n" +
                        "          );\n" +
                        "        </intension>\n" +
                        "        <extension>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (13, 'Young Nastyman', 'Flight');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (12, 'Professor X', 'Telepathy');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (15, 'Mystique', 'Shapeshifting');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (13, 'Superman', 'Telepathy');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (18, 'Wonderboy', 'Telekinesis');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (12, 'Magneto', 'Magnetism');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (21, 'Young Nastyman', 'Mind Bullets');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (21, 'Superman', 'Flight');</tuple>\n" +
                        "          <tuple>INSERT INTO HasPowerSince VALUES (12, 'Wonderboy', 'Levitation');</tuple>\n" +
                        "        </extension>\n" +
                        "      </relation>\n" +
                        "      <relation>\n" +
                        "        <intension>\n" +
                        "          CREATE TABLE SuperHero(\n" +
                        "            alias VARCHAR(255) NOT NULL REFERENCES PoweredPerson(alias),\n" +
                        "            cape BOOLEAN,\n" +
                        "            PRIMARY KEY(alias)\n" +
                        "          );\n" +
                        "        </intension>\n" +
                        "        <extension>\n" +
                        "          <tuple>INSERT INTO SuperHero VALUES ('Wonderboy', NULL);</tuple>\n" +
                        "          <tuple>INSERT INTO SuperHero VALUES ('Superman', TRUE);</tuple>\n" +
                        "          <tuple>INSERT INTO SuperHero VALUES ('Professor X', FALSE);</tuple>\n" +
                        "        </extension>\n" +
                        "      </relation>\n" +
                        "      <relation>\n" +
                        "        <intension>\n" +
                        "          CREATE TABLE SuperVillain(\n" +
                        "            alias VARCHAR(255) NOT NULL REFERENCES PoweredPerson(alias),\n" +
                        "            PRIMARY KEY(alias)\n" +
                        "          );\n" +
                        "        </intension>\n" +
                        "        <extension>\n" +
                        "          <tuple>INSERT INTO SuperVillain VALUES ('Magneto');</tuple>\n" +
                        "          <tuple>INSERT INTO SuperVillain VALUES ('Mystique');</tuple>\n" +
                        "          <tuple>INSERT INTO SuperVillain VALUES ('Young Nastyman');</tuple>\n" +
                        "        </extension>\n" +
                        "      </relation>\n" +
                        "    </schema>\n" +
                        "    \n" +
                        "    <subtasks>\n" +
                        "      \n" +
                        "      <subtask>\n" +
                        "      <subtaskid>1</subtaskid>" +
                        "        <tasktexts>\n" +
                        "          <tasktext language=\"EN\">The super villain table is an excerpt of the powered person table. Provide a query on the powered person table that reproduces the super villain table.</tasktext>\n" +
                        "        </tasktexts>\n" +
                        "        <solution>\n" +
                        "          <referencestatement>\n" +
                        "            SELECT alias\n" +
                        "            FROM PoweredPerson\n" +
                        "            WHERE evilness_level > 0;\n" +
                        "          </referencestatement>\n" +
                        "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                        "        </solution>\n" +
                        "        <points>1</points>\n" +
                        "      </subtask>\n" +
                        "      \n" +
                        "      <subtask>\n" +
                        "      <subtaskid>2</subtaskid>" +
                        "        <tasktexts>\n" +
                        "          <tasktext language=\"EN\">A query for all super heroes having no cape.</tasktext>\n" +
                        "        </tasktexts>\n" +
                        "        <solution>\n" +
                        "          <referencestatement>\n" +
                        "            SELECT *\n" +
                        "            FROM SuperHero\n" +
                        "            WHERE cape = 'FALSE';\n" +
                        "          </referencestatement>\n" +
                        "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                        "        </solution>\n" +
                        "        <points>1</points>\n" +
                        "      </subtask>\n" +
                        "      \n" +
                        "      <subtask>\n" +
                        "      <subtaskid>3</subtaskid>" +
                        "        <tasktexts>\n" +
                        "          <tasktext language=\"EN\">A query for the names of all powers of Young Nastyman and the number of the comic they first appeared in, ordered by their appearance (newest first). If two powers appeared at the same time, sort them alphabetically by their name.</tasktext>\n" +
                        "        </tasktexts>\n" +
                        "        <solution>\n" +
                        "          <referencestatement>\n" +
                        "            SELECT power, comic\n" +
                        "            FROM HasPowerSince\n" +
                        "            WHERE alias = 'Young Nastyman'\n" +
                        "            ORDER BY comic DESC, power ASC;\n" +
                        "          </referencestatement>\n" +
                        "          <evaluationstrategy>LIST</evaluationstrategy>\n" +
                        "        </solution>\n" +
                        "        <requiredterms>\n" +
                        "          <term>ORDER BY</term>\n" +
                        "        </requiredterms>\n" +
                        "        <points>2</points>\n" +
                        "      </subtask>\n" +
                        "      \n" +
                        "      <subtask>\n" +
                        "      <subtaskid>4</subtaskid>" +
                        "        <tasktexts>\n" +
                        "          <tasktext language=\"EN\">A query for the number of pages I would have to read if I wanted to read all Marvel comics in the database.</tasktext>\n" +
                        "        </tasktexts>\n" +
                        "        <solution>\n" +
                        "          <referencestatement>\n" +
                        "            SELECT SUM(pages) AS sum\n" +
                        "            FROM comic\n" +
                        "            WHERE publisher = 'Marvel';\n" +
                        "          </referencestatement>\n" +
                        "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                        "        </solution>\n" +
                        "        <requiredterms>\n" +
                        "          <term>SUM</term>\n" +
                        "        </requiredterms>\n" +
                        "        <points>2</points>\n" +
                        "      </subtask>\n" +
                        "      \n" +
                        "      <subtask>\n" +
                        "      <subtaskid>5</subtaskid>" +
                        "        <tasktexts>\n" +
                        "          <tasktext language=\"EN\">A query that states if the average powered person is evil (evilness > 0) or good (evilness = 0), depending on all powered persons evilness levels.</tasktext>\n" +
                        "        </tasktexts>\n" +
                        "        <solution>\n" +
                        "          <referencestatement>\n" +
                        "            SELECT AVG(evilness_level)\n" +
                        "            FROM PoweredPerson;\n" +
                        "          </referencestatement>\n" +
                        "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                        "        </solution>\n" +
                        "        <points>2</points>\n" +
                        "      </subtask>\n" +
                        "      \n" +
                        "      \n" +
                        "      \n" +
                        "    </subtasks>\n" +
                        "    \n" +
                        "  </task>\n" +
                        "  \n" +
                        "</tasks>";

        try {
            TaskFile.create(null, xml, false);
        } catch (SQLAlchemistException e) {
            Logger.info(e.getMessage());
        }

        Logger.info("TaskFile.init - Done initializing");
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
/*
The super villain table is an excerpt of the powered person table. Provide a query on the powered person table that reproduces the super villain table.

SELECT alias
  FROM PoweredPerson
  WHERE evilness_level > 0

A query for all super heroes having no cape.
SELECT *
  FROM SuperHero
  WHERE cape = 'FALSE'

A query for the names of all powers of Young Nastyman and the number of the comic they first appeared in, ordered by their appearance (newest first). If two powers appeared at the same time, sort them alphabetically by their name.
SELECT power, comic
 FROM HasPowerSince
 WHERE alias = 'Young Nastyman'
 ORDER BY comic DESC, power ASC

A query for the number of pages I would have to read if I wanted to read all Marvel comics in the database.
SELECT SUM(pages) AS sum
  FROM comic
  WHERE publisher = 'Marvel'

A query that states if the average powered person is evil (evilness > 0) or good (evilness ≤ 0), depending on all powered persons’ evilness levels.
SELECT AVG(evilness_level)
  FROM PoweredPerson

 */

}
