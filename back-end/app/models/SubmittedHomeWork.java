package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dao.HomeWorkChallengeDAO;

import play.Logger;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

/**
 * Created by invisiblevm on 7/13/15.
 */
@Entity
@Table(
        name = "submitted_homework",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "sub_task_id, home_work_id"})
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SubmittedHomeWork extends SolvedSubTask {
    @Id
    long id;

    @ManyToOne
    @Column(name = "home_work_id")
    HomeWorkChallenge homeWork;

    String statement;
    boolean solve;

    private static Finder<Long, SubmittedHomeWork> find = new Finder<>(Long.class, SubmittedHomeWork.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public SubmittedHomeWork (
            Profile profile,
            SubTask subTask,
            HomeWorkChallenge homeWork,
            boolean solve,
            String statement) {

        super(profile, subTask);
        this.homeWork = homeWork;

        this.solve = solve;
        this.statement = statement;
    }

//////////////////////////////////////////////////
//  Getter Object
//////////////////////////////////////////////////

    public static List<SubmittedHomeWork> getSubmitsForSubtask(SubTask subTask) {
        return find.where().eq("sub_task_id", subTask.getId()).findList();
    }

    public static List<Object> getSubmitsForProfile(Profile profile) {
        return find.where().eq("profile_id", profile.getId()).findIds();
    }

    public static List<SubmittedHomeWork> getSubmitsForSubtaskAndHomeWorkChallenge(long subTaskId, long homeWorkChallengeId) {
        return find.where().eq("sub_task_id", subTaskId).eq("home_work_id", homeWorkChallengeId).findList();
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    public static ArrayNode toJsonAll(List<SubmittedHomeWork> submittedHomeWorks) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (SubmittedHomeWork submittedHomeWork : submittedHomeWorks) {
            arrayNode.add(submittedHomeWork.toJson());
        }

        return arrayNode;
    }

    public ObjectNode toJson() {
        ObjectNode objectNode = Json.newObject();

        objectNode.put("id", this.id);
        objectNode.put("statement", this.statement);
        objectNode.put("solve", this.solve);
        objectNode.put("student", this.profile.getUser().toJson());
        objectNode.put("sub_task", this.subTask.getId());

        return objectNode;
    }

//////////////////////////////////////////////////
//  Submit Method
//////////////////////////////////////////////////

    public static SubmittedHomeWork submit(
            Profile profile,
            SubTask subTask,
            boolean solve,
            String statement) {

        if (HomeWorkChallengeDAO.getCurrent() == null) {
            Logger.warn("Trying to submit without having an active HomeWork!!!");
            return null;
        }

        if (!HomeWorkChallengeDAO.getCurrent().contains(subTask)) {
            Logger.info("SubmittedHomeWork.submit - SomeOne got Late");
            return null;
        }

        SubmittedHomeWork existed = find.where().eq("profile_id", profile.getId()).eq("sub_task_id", subTask.getId()).eq("home_work_id", HomeWorkChallengeDAO.getCurrent().getId()).findUnique();

        if (existed != null) {
            try {
                existed.solve = solve;
                existed.statement = statement;

                existed.update();
                return existed;
            } catch (PersistenceException pe) {
                Logger.warn("Cannot Submit: " + pe.getMessage());
            }
        }


        SubmittedHomeWork submittedHomeWork = new SubmittedHomeWork(
                profile,
                subTask,
                HomeWorkChallengeDAO.getCurrent(),
                solve,
                statement);

        try {
            submittedHomeWork.save();

            return submittedHomeWork;
        } catch (PersistenceException pe) {
            Logger.warn("Not saving: " + pe.getMessage());
            return null;
        }
    }

//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////

    public static void init () {

        Logger.info("Initialize 'SubmittedHW' data");


        Logger.info("Done initializing");
    }

}
