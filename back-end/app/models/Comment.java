package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;


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
//  Getter & Setter
//////////////////////////////////////////////////

    public long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getText() {
        return text;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
