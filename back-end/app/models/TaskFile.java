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

    public static final Finder<Long, TaskFile> find = new Finder<>(Long.class, TaskFile.class);

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
    public TaskFile(
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



        objectNode.put("fileName",      this.fileName);

        return objectNode;
    }

    public ObjectNode toHomeWorkJsonForProfile(Profile profile) {
        ObjectNode  objectNode = Json.newObject();

        ArrayNode   subTasks   = JsonNodeFactory.instance.arrayNode();

        for(SubTask subTask : this.subTasks) {
            subTasks.add(subTask.toHomeWorkJsonForProfile(profile));
        }

        objectNode.put("fileName",      this.fileName);
        objectNode.put("schema",        this.schema);
        objectNode.put("subTasks",      subTasks);

        return objectNode;
    }

//////////////////////////////////////////////////
//  action
//////////////////////////////////////////////////

    /**
     * add subtasks of a List to own subtask list
     * @param subTaskList   a list filled with subtasks
     */
    public void addSubTask(List<SubTask> subTaskList)
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



    public void makeAvailable() {
        List<SubTask> subTaskList = this.getSubTasks();

        for (SubTask subTask : subTaskList) {
            subTask.makeAvailable();
            subTask.update();
        }

        this.setAvailable(true);
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
