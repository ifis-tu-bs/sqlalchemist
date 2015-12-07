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
        name = "submittedhomework",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "task_id", "home_work_id"})
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SubmittedHomeWork extends Model {
    @Id
    private Long id;

    @ManyToOne
    final User user;

    @ManyToOne
    final Task task;

    @ManyToOne
    final HomeWork homeWork;

    private int syntaxChecksDone;
    private int semanticChecksDone;
    
    @Column(columnDefinition = "Text")
    String statement;
    boolean solve;

    public static final Finder<Long, SubmittedHomeWork> find = new Finder<>(SubmittedHomeWork.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////



    public SubmittedHomeWork (
            User user,
            Task task,
            HomeWork homeWork) {

        this.user = user;
        this.task = task;
        this.homeWork = homeWork;
        this.syntaxChecksDone = 0;
        this.semanticChecksDone = 0;
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
        objectNode.set("student", this.user.toJsonUser());
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

    public void submit(boolean solved, String statement) {
        this.solve = solved;
        this.statement = statement;
    }
}
