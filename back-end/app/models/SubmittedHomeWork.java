package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

import javax.persistence.*;
import java.util.List;

/**
 * Created by invisiblevm on 7/13/15.
 *
 * @author invisiblevm
 */
@Entity
@Table(
        name = "submitted_homework",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "task_id, home_work_id"})
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SubmittedHomeWork extends SolvedTask {
    @Id
    long id;

    @ManyToOne
    @Column(name = "home_work_id")
    HomeWork homeWork;

    String statement;
    boolean solve;

    public static Finder<Long, SubmittedHomeWork> find = new Finder<>(Long.class, SubmittedHomeWork.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public SubmittedHomeWork (
            Profile profile,
            Task task,
            HomeWork homeWork,
            boolean solve,
            String statement) {

        super(profile, task);
        this.homeWork = homeWork;

        this.solve = solve;
        this.statement = statement;
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
        objectNode.put("task", this.task.getId());

        return objectNode;
    }

    public HomeWork getHomeWork() {
      return this.homeWork;
    }

    public String getStatement() {
      return this.statement;
    }

    public void setStatement(String statement) {
      this.statement = statement;
    }

    public boolean getSolve() {
      return this.solve;
    }

    public void setSolve(boolean solve) {
      this.solve = solve;
    }
}
