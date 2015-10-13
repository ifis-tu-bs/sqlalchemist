package models;

import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by stefa_000 on 16.06.2015.
 */

/**
 * This model describes the comment.
 * Every comment is related to a subTask, the profile of the user, who created the comment and the content (comment) as a string.
 */
@Entity
@Table(name = "Comment")
public class Comment extends Model{

    /** auto-generated ID for this comment. */
    @Id
    private long id;

    /** Relation to the profile of the user, who created the comment. */
    @ManyToOne
    private Profile profile;

    /** Content of the comment. */
    @Column(name = "text")
    private String text;

    private final Date created_at;

    /**
     * Finder for this class
     */
    public static final Model.Finder<Long, Comment> find = new Model.Finder<>(
            Long.class, Comment.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public Comment(Profile profile, String text) {
        this.profile    = profile;
        this.text       = text;

        this.created_at = new Date();
    }


//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("profile", this.profile.toJsonProfile());
        node.put("text",    this.text);
        node.put("written", String.valueOf(this.created_at));

        return node;
    }


    public static ArrayNode toJsonAll(List<Comment> commentList) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for(Comment comment : commentList) {
            arrayNode.add(comment.toJson());
        }

        return arrayNode;
    }
}
