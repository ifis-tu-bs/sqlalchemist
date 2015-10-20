package models;

import com.avaje.ebean.Model;
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
        name = "SubmittedHomework",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "task_id", "home_work_id"})
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SubmittedHomeWork extends Model {
    @Id
    private Long id;

    @ManyToOne
    final Profile profile;

    @ManyToOne
    final Task task;

    @ManyToOne
    @Column(name = "home_work_id")
    final HomeWork homeWork;

    private int syntaxChecksDone;
    private int semanticChecksDone;

    String statement;
    boolean solve;

    public static final Finder<Long, SubmittedHomeWork> find = new Finder<>(Long.class, SubmittedHomeWork.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////



    public SubmittedHomeWork (
            Profile profile,
            Task task,
            HomeWork homeWork,
            boolean solve,
            String statement) {

        this.profile = profile;
        this.task = task;
        this.homeWork = homeWork;
        this.syntaxChecksDone = 0;
        this.semanticChecksDone = 0;

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
        objectNode.set("student", this.profile.getUser().toJson());
        objectNode.put("task", this.task.getId());

        return objectNode;
    }

    public void addSyntaxCheck() {
        this.syntaxChecksDone++;
    }

    public void addSemanticCheck() {
        this.semanticChecksDone++;
    }

    public int getSemanticChecksDone() {
        return semanticChecksDone;
    }

    public int getSyntaxChecksDone() {
        return syntaxChecksDone;
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
