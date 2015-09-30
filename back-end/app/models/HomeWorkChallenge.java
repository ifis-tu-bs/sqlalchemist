package models;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by fabiomazzone on 11/06/15.
 */
@Entity
@Table(name = "homeWork_challenge")
public class HomeWorkChallenge extends Challenge {

    @Id
    private long id;

    @ManyToOne
    private Profile creator;

    public static final int CHALLENGE_TYPE_HOMEWORK = 2;


    @ManyToMany(cascade = CascadeType.ALL)
    protected final List<TaskFile> taskFiles;

    private Date start_at;

    private Date expires_at;

    public static final Finder<Long, HomeWorkChallenge> find = new Finder<>(Long.class, HomeWorkChallenge.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public HomeWorkChallenge(
            String name,
            Profile creator,
            int solve_type,
            int solve_type_extension,
            List<TaskFile> taskFiles,
            int type,
            Date start_at,
            Date expires_at) {

        super(name,
                solve_type,               // -\ These
                solve_type_extension,     // _/ do?
                CHALLENGE_TYPE_HOMEWORK); // -----#
                                          //      |
        this.creator = creator;           //      #-- Same?
        this.taskFiles = taskFiles;       //      |
        this.type = type;                 // -----#
        this.start_at = start_at;
        this.expires_at = expires_at;

        this.save();
    }

//////////////////////////////////////////////////
//  Object Getter
//////////////////////////////////////////////////


    public List<TaskFile> getTaskFiles() {
        return this.taskFiles;
    }

    public long getId() {
        return this.id;
    }


//////////////////////////////////////////////////
//  Object Methods
//////////////////////////////////////////////////

    public String getName() {
        return this.name;
    }

    public Date getExpires_at() {
        return this.expires_at;
    }

    public boolean contains(SubTask subTask) {
        for (TaskFile taskFile : taskFiles) {
            if (taskFile.contains(subTask))
                return true;
        }
        return false;
    }

    public boolean submittedAll(List<Object> submits) {
        boolean answer = true;
        for (TaskFile taskFile : this.taskFiles) {
            answer &= taskFile.submittedAll(submits);
        }
        return answer;
    }
//////////////////////////////////////////////////
//  Json Methods
//////////////////////////////////////////////////

    /**
     *  Makes Json Object for this Object
     * @return
     */
    public ObjectNode toJson() {
        ObjectNode objectNode = Json.newObject();


        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        for (TaskFile taskFile : taskFiles) {
            arrayNode.add(taskFile.toJson());
        }

        objectNode.put("id", this.id);
        objectNode.put("name", this.name);
        objectNode.put("taskFiles", arrayNode);
        objectNode.put("creator", this.creator.toJsonProfile());
        objectNode.put("start_at", String.valueOf(this.start_at));
        objectNode.put("expires_at", String.valueOf(this.expires_at));

        return objectNode;
    }
}
