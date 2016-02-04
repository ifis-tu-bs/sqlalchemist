package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dao.RoleDAO;
import play.Logger;
import play.data.validation.ValidationError;

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
    private PermissionRules ownTaskSetPermissions       = PermissionRules.getNoControl();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "foreign_task_set_create")),
            @AttributeOverride(name="read",     column=@Column(name = "foreign_task_set_read")),
            @AttributeOverride(name="update",   column=@Column(name = "foreign_task_set_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "foreign_task_set_delete"))
    })
    private PermissionRules foreignTaskSetPermissions   = PermissionRules.getNoControl();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "own_task_create")),
            @AttributeOverride(name="read",     column=@Column(name = "own_task_read")),
            @AttributeOverride(name="update",   column=@Column(name = "own_task_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "own_task_delete"))
    })
    private PermissionRules ownTaskPermissions          = PermissionRules.getNoControl();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "foreign_task_create")),
            @AttributeOverride(name="read",     column=@Column(name = "foreign_task_read")),
            @AttributeOverride(name="update",   column=@Column(name = "foreign_task_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "foreign_task_delete"))
    })
    private PermissionRules foreignTaskPermissions      = PermissionRules.getNoControl();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "homework_create")),
            @AttributeOverride(name="read",     column=@Column(name = "homework_read")),
            @AttributeOverride(name="update",   column=@Column(name = "homework_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "homework_delete"))
    })
    private PermissionRules homeworkPermissions         = PermissionRules.getNoControl();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "role_create")),
            @AttributeOverride(name="read",     column=@Column(name = "role_read")),
            @AttributeOverride(name="update",   column=@Column(name = "role_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "role_delete"))
    })
    private PermissionRules rolePermissions             = PermissionRules.getNoControl();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "group_create")),
            @AttributeOverride(name="read",     column=@Column(name = "group_read")),
            @AttributeOverride(name="update",   column=@Column(name = "group_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "group_delete"))
    })
    private PermissionRules groupPermissions             = PermissionRules.getNoControl();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="create",   column=@Column(name = "user_create")),
            @AttributeOverride(name="read",     column=@Column(name = "user_read")),
            @AttributeOverride(name="update",   column=@Column(name = "user_update")),
            @AttributeOverride(name="delete",   column=@Column(name = "user_delete"))
    })
    private PermissionRules userPermissions             = PermissionRules.getNoControl();

    private int             votes;

    private boolean         isDeletable = true;

    @OneToMany(mappedBy = "role")
    private List<User>      assignedUser;

    @ManyToOne
    private User            creator;

    private Calendar        createdAt = Calendar.getInstance();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Getter
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public long getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public PermissionRules getOwnTaskSetPermissions() {
        return ownTaskSetPermissions;
    }

    public void setOwnTaskSetPermissions(PermissionRules ownTaskSetPermissions) {
        this.ownTaskSetPermissions = ownTaskSetPermissions;
    }

    public PermissionRules getForeignTaskSetPermissions() {
        return foreignTaskSetPermissions;
    }

    public void setForeignTaskSetPermissions(PermissionRules foreignTaskSetPermissions) {
        this.foreignTaskSetPermissions = foreignTaskSetPermissions;
    }

    public PermissionRules getOwnTaskPermissions() {
        return ownTaskPermissions;
    }

    public void setOwnTaskPermissions(PermissionRules ownTaskPermissions) {
        this.ownTaskPermissions = ownTaskPermissions;
    }

    public PermissionRules getForeignTaskPermissions() {
        return foreignTaskPermissions;
    }

    public void setForeignTaskPermissions(PermissionRules foreignTaskPermissions) {
        this.foreignTaskPermissions = foreignTaskPermissions;
    }

    public PermissionRules getHomeworkPermissions() {
        return homeworkPermissions;
    }

    public void setHomeworkPermissions(PermissionRules homeworkPermissions) {
        this.homeworkPermissions = homeworkPermissions;
    }

    public PermissionRules getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(PermissionRules rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public PermissionRules getGroupPermissions() {
        return groupPermissions;
    }

    public void setGroupPermissions(PermissionRules groupPermissions) {
        this.groupPermissions = groupPermissions;
    }

    public PermissionRules getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(PermissionRules userPermissions) {
        this.userPermissions = userPermissions;
    }

    @JsonProperty("votes")
    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @JsonProperty("deletable")
    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    @JsonIgnore
    public User getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public String getCreatorName() {
        return (creator != null) ? creator.getUsername() : null;
    }

    @JsonIgnore
    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Persistence Methods
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void delete() {
        Role role = RoleDAO.getLowerRole(priority);
        if(role == null) {
            role = RoleDAO.getNextHigherRole(priority);
        }
        for(User user : this.assignedUser) {
            Logger.info(user.getEmail());
            user.setRole(role);
            user.update();
        }

        super.delete();
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", priority=" + priority +
                ", roleName='" + roleName + '\'' +
                ", ownTaskSetPermissions=" + ownTaskSetPermissions +
                ", foreignTaskSetPermissions=" + foreignTaskSetPermissions +
                ", ownTaskPermissions=" + ownTaskPermissions +
                ", foreignTaskPermissions=" + foreignTaskPermissions +
                ", homeworkPermissions=" + homeworkPermissions +
                ", rolePermissions=" + rolePermissions +
                ", groupPermissions=" + groupPermissions +
                ", userPermissions=" + userPermissions +
                ", votes=" + votes +
                ", isDeletable=" + isDeletable +
                ", assignedUser=" + assignedUser +
                ", creator=" + creator +
                ", createdAt=" + createdAt +
                '}';
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Attribute Helper
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @JsonIgnore
    public void signOffUser(User user) {
        this.assignedUser.remove(user);
    }

    @JsonIgnore
    public void signOnUser(User user) {
        this.assignedUser.add(user);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Form Helper
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @JsonIgnore
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if(RoleDAO.getByPriority(priority) != null) {
            errors.add(new ValidationError("priority", "priority already taken"));
        }
        if(RoleDAO.getByRoleName(roleName) != null) {
            errors.add(new ValidationError("roleName", "roleName already taken"));
        }

        return errors.isEmpty() ? null : errors;
    }
}
