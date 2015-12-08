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
    private PermissionRules ownTaskSetPermissions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "foreign_task_set_create")),
            @AttributeOverride(name="read",     column=@Column(name = "foreign_task_set_read")),
            @AttributeOverride(name="update",   column=@Column(name = "foreign_task_set_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "foreign_task_set_delete"))
    })
    private PermissionRules foreignTaskSetPermissions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "own_task_create")),
            @AttributeOverride(name="read",     column=@Column(name = "own_task_read")),
            @AttributeOverride(name="update",   column=@Column(name = "own_task_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "own_task_delete"))
    })
    private PermissionRules ownTaskPermissions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "foreign_task_create")),
            @AttributeOverride(name="read",     column=@Column(name = "foreign_task_read")),
            @AttributeOverride(name="update",   column=@Column(name = "foreign_task_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "foreign_task_delete"))
    })
    private PermissionRules foreignTaskPermissions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "homework_create")),
            @AttributeOverride(name="read",     column=@Column(name = "homework_read")),
            @AttributeOverride(name="update",   column=@Column(name = "homework_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "homework_delete"))
    })
    private PermissionRules homeworkPermissions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "role_create")),
            @AttributeOverride(name="read",     column=@Column(name = "role_read")),
            @AttributeOverride(name="update",   column=@Column(name = "role_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "role_delete"))
    })
    private PermissionRules rolePermissions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "user_create")),
            @AttributeOverride(name="read",     column=@Column(name = "user_read")),
            @AttributeOverride(name="update",   column=@Column(name = "user_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "user_delete"))
    })
    private PermissionRules userPermissions;

    private boolean         isDeletable = true;

    private List<User>      assignedUser;

    private User            creator;

    private Calendar        createdAt;

    public Role(
            int         priority,
            String      roleName,
            PermissionRules ownTaskSet,
            PermissionRules foreignTaskSet,
            PermissionRules ownTask,
            PermissionRules foreignTask,
            PermissionRules homework,
            PermissionRules role,
            PermissionRules user,
            boolean     isDeletable,
            User        creator) {

        this.priority                   = priority;
        this.roleName                   = roleName;
        this.ownTaskSetPermissions      = ownTaskSet;
        this.foreignTaskSetPermissions  = foreignTaskSet;
        this.ownTaskPermissions         = ownTask;
        this.foreignTaskPermissions     = foreignTask;
        this.homeworkPermissions        = homework;
        this.rolePermissions            = role;
        this.userPermissions            = user;
        this.isDeletable                = isDeletable;
        this.assignedUser               = new ArrayList<>();
        this.creator                    = creator;

        this.createdAt                  = Calendar.getInstance();
    }


    public PermissionRules getRolePermissions() {
        return rolePermissions;
    }

    public void signOffUser(User user) {
        this.assignedUser.remove(user);
    }

    public void signOnUser(User user) {
        this.assignedUser.add(user);
    }
}
