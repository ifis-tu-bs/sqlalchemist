package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.*;
import sqlgame.exception.MySQLAlchemistException;
import sqlgame.sandbox.*;
import Exception.SQLAlchemistException;
import helper.Random;

import dao.CommentDAO;

import play.*;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(
        name = "subTask",
        uniqueConstraints = @UniqueConstraint(columnNames = {"task_file_id", "position"})
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SubTask extends Model {
    @Id
    private long id;

    @OneToOne
    private Profile creator;
    @OneToOne
    private TaskFile    taskFile;

    @Column(name = "position")
    private int         index;
    @Column(name = "subTask_Points")
    private int points;
    private String exercise;
    @Column(columnDefinition = "text")
    private String refStatement;

    @Column(name = "available")
    private boolean isAvailable;
    private boolean isHomeWork;

    @ManyToMany
    private List<Comment> commentList;

    @ManyToMany
    private List<Rating> ratings;

    private Date created_at;
    private Date edited_at;

    private static final Finder<Long, SubTask> find = new Finder<>(Long.class, SubTask.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    /**
     *
     * @param creator
     * @param taskFile
     * @param index
     * @param points
     * @param taskTexts
     * @param refStatement
     */
    public SubTask(
            Profile creator,
            TaskFile taskFile,
            int index,
            int points,
            String taskTexts,
            String refStatement,
            boolean isHomeWork
    ) {
        super();

        this.creator        = creator;
        this.taskFile       = taskFile;
        this.index          = index;
        this.points         = points;
        this.exercise       = taskTexts;
        this.refStatement   = refStatement;
        this.isAvailable    = false;
        this.isHomeWork     = isHomeWork;
        this.commentList    = new ArrayList<>();
        this.ratings        = new ArrayList<>();
        this.created_at     = new Date();
    }

    @Override
    public void update() {
        this.edited_at = new Date();
        super.update();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if(!(o instanceof SubTask)) return false;

        SubTask subTask = (SubTask) o;

        return id == subTask.id;

    }

//////////////////////////////////////////////////
//  json method
//////////////////////////////////////////////////

    public ObjectNode toJson() {
        ObjectNode json = Json.newObject();

        Rating      rating_sum  = new Rating();
        rating_sum.add(this.ratings);

        json.put("id",          this.id);
        if(creator != null) {
            json.put("create", this.creator.toJsonProfile());
        }
        json.put("taskFile",    this.taskFile.getId());
        json.put("index",       this.index);
        json.put("points",      this.points);
        json.put("exercise",    this.exercise);
        json.put("refStatement",this.refStatement.replaceAll(" +", " ").replaceAll("\\n ", "\n").trim());
        json.put("available",   this.isAvailable);
        json.put("rating",      rating_sum.toJson());

        json.put("created_at", String.valueOf(this.created_at));
        return json;
    }

    /**
     *
     * @return
     */
    public ObjectNode toJsonExercise() {
        ObjectNode json = Json.newObject();

/* JSON.subTask:
        id                     :int
        schema                 :String
        exercise               :String
        points                 :int
        timeLimit              :int
        difficulty             :int
        rating                 :JSON.rating*/

        Rating      rating_sum  = new Rating();
        rating_sum.add(this.ratings);

        json.put("id", this.id);
        json.put("schema", this.taskFile.getSchema());
        json.put("exercise", this.exercise);
        json.put("points", this.points);
        json.put("rating",      rating_sum.toJson());

        return json;
    }


//////////////////////////////////////////////////
//  Getter & Setter
//////////////////////////////////////////////////

    public List<Rating> getRatings() {
        return this.ratings;
    }

    public void setHomeWork() {
        this.isHomeWork = true;
    }

    public TaskFile getTaskFile() {
        return this.taskFile;
    }

    public long getId() {
        return this.id;
    }

//////////////////////////////////////////////////
//  Action
//////////////////////////////////////////////////

    public boolean comment(Profile profile, String text) {
        Comment comment;
        if((comment = CommentDAO.create(profile, text)) != null) {
            this.commentList.add(comment);
            return true;
        }
        return false;
    }


    /**
     * This is the method to rate this TaskFile
     *
     * @param profile       the profile thats rate
     * @param positive      if the rating for positive
     * @param needReview    if the rating for needReview
     * @param negative      if the rating for negative
     * @return              return true if the rating process is successful
     */
    public boolean rate(Profile profile, boolean positive, boolean needReview, boolean negative) {
        Rating rating = null;

        if(this.ratings != null && this.ratings.size() > 0) {
            for (Rating ratingI : this.ratings) {
                if (ratingI.getProfile().getId() == profile.getId()) {
                    rating = ratingI;
                    break;
                }

            }
        } else {
            this.ratings = new ArrayList<>();
        }

        if(rating != null) {
            rating.rate(positive, needReview, negative);
            rating.update();
            return true;
        } else {
            rating = Rating.create(profile, positive, needReview, negative);
            if(rating == null) {
                return false;
            }
            this.ratings.add(rating);
        }
        return true;
    }

    public static final int TASK_SOLVE_STATEMENT_CORRECT = 0;
    public static final int TASK_SOLVE_STATEMENT_ERROR = 1;

    /**
     *
     * @param statement
     * @return
     */
    public boolean solve(String statement) throws SQLAlchemistException {
        Task task = this.taskFile.getTask();
        try {
            task.startTask("local");
        } catch (MySQLAlchemistException e) {
            throw new SQLAlchemistException("Database Exception, try later");
        }
        try {
            boolean status = task.isUserStatementCorrect(statement, this.index);

            task.closeTask();
            Logger.info("status: " + status);
            return status;
        } catch (MySQLAlchemistException e) {
            Logger.warn("SubTask.solve - catches MySQLAlchemistException: " + e.getMyMessage());
            try {
                task.closeTask();
            } catch (MySQLAlchemistException e1) {}
            throw new SQLAlchemistException(e.getMyMessage());
        }
    }


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
            points = scroll.getPotion().getLevel();
        } else {
            List<ScrollCollection> scrollList = ScrollCollection.getScrollCollection(profile);
            for(ScrollCollection scrollCollection : scrollList) {
                Scroll singleScroll = scrollCollection.scroll;
                if( !singleScroll.isRecipe() && singleScroll.getType() == scroll.getType()) {
                    points++;
                }
            }
        }
        Logger.info("Diffi: " + Math.round(points / 2));
        return getByDifficulty(profile, Math.round(points / 2));
    }

    public static SubTask getByDifficulty(Profile profile, int points) {
        List<SubTask> subTaskList           = find.where().eq("available", true).eq("points", points).findList(); //.eq("is_home_work", false).findList();
        List<SolvedSubTask> solvedSubTasks  = SolvedSubTask.getAllDoneSubTask(profile);

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
            subTaskList           = find.where().eq("available", true).eq("points", points).findList();
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
        SubTask subTask = find.where().eq("taskFile", taskFile).eq("index", index).findUnique();
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
        List<SubTask> subTaskList = find.all();
        if (subTaskList == null) {
            Logger.warn("SubTask.getAll - no entitys saved");
            return null;
        }
        return subTaskList;
    }

    /**
     * this method adds comments to the SubTask
     *
     * @param comment   the comment to be added
     */
    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public List<Comment> getCommentList() {
        return this.commentList;
    }

    public int getScore() {
        return (this.points * 100) * 50;
    }

    public void makeAvailable() {
        this.setAvailable(true);
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
