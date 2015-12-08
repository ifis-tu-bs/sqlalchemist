package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * the role class
 */
@Entity
@Table(name = "role")
public class Role extends Model {
    @Id
    private long            id;

    @Column(unique = true)
    private int             priority;

    @Column(unique = true, nullable = false)
    private String          roleName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "own_task_set_create")),
            @AttributeOverride(name="read",     column=@Column(name = "own_task_set_read")),
            @AttributeOverride(name="update",   column=@Column(name = "own_task_set_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "own_task_set_delete"))
    })
    private ActionRule      ownTaskSet;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "foreign_task_set_create")),
            @AttributeOverride(name="read",     column=@Column(name = "foreign_task_set_read")),
            @AttributeOverride(name="update",   column=@Column(name = "foreign_task_set_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "foreign_task_set_delete"))
    })
    private ActionRule      foreignTaskSet;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "own_task_create")),
            @AttributeOverride(name="read",     column=@Column(name = "own_task_read")),
            @AttributeOverride(name="update",   column=@Column(name = "own_task_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "own_task_delete"))
    })
    private ActionRule      ownTask;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "foreign_task_create")),
            @AttributeOverride(name="read",     column=@Column(name = "foreign_task_read")),
            @AttributeOverride(name="update",   column=@Column(name = "foreign_task_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "foreign_task_delete"))
    })
    private ActionRule      foreignTask;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "homework_create")),
            @AttributeOverride(name="read",     column=@Column(name = "homework_read")),
            @AttributeOverride(name="update",   column=@Column(name = "homework_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "homework_delete"))
    })
    private ActionRule      homework;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "role_create")),
            @AttributeOverride(name="read",     column=@Column(name = "role_read")),
            @AttributeOverride(name="update",   column=@Column(name = "role_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "role_delete"))
    })
    private ActionRule      role;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "user_create")),
            @AttributeOverride(name="read",     column=@Column(name = "user_read")),
            @AttributeOverride(name="update",   column=@Column(name = "user_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "user_delete"))
    })
    private ActionRule      user;

    private boolean         isDeletable = true;

    private List<User>      assigendUser;

    private User            creator;

    private Calendar        createdAt;

    public Role(
            int         priority,
            String      roleName,
            ActionRule ownTaskSet,
            ActionRule foreignTaskSet,
            ActionRule ownTask,
            ActionRule foreignTask,
            ActionRule homework,
            ActionRule role,
            ActionRule user,
            boolean     isDeletable,
            User        creator) {

        this.priority       = priority;
        this.roleName       = roleName;
        this.ownTaskSet     = ownTaskSet;
        this.foreignTaskSet = foreignTaskSet;
        this.ownTask        = ownTask;
        this.foreignTask    = foreignTask;
        this.homework       = homework;
        this.role           = role;
        this.user           = user;
        this.isDeletable    = isDeletable;
        this.assigendUser   = new ArrayList<>();
        this.creator        = creator;

        this.createdAt      = Calendar.getInstance();
    }

    public void signOffUser(User user) {
        this.assigendUser.remove(user);
    }

    public void signOnUser(User user) {
        this.assigendUser.add(user);
    }
}
