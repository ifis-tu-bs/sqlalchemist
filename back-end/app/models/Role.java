package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * the role class
 */
@Entity
@Table(name = "role")
public class Role extends Model {
    @Id
    private long        id;

    @Column(unique = true)
    private String      name;

    private boolean     TasksBrowse     = false;
    private boolean     TasksCreate     = false;
    private boolean     TasksAdmin      = false;

    private boolean     RolesAdmin      = false;
    private boolean     UserAdmin       = false;

    private boolean     HomeworkCreate  = false;
    private boolean     HomeworkBrowse  = false;

    @OneToOne
    private Role        followingRole   = null;

    @OneToOne(mappedBy = "role", targetEntity = User.class)
    private User        creator;
    private Calendar    created_at;

    @OneToMany(mappedBy = "role", targetEntity = User.class)
    private User        updated_from;
    private Calendar    updated_at;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Constructor
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * the role constructor
     * @param name      the name of the role
     * @param creator   the create of the role
     */
    public Role(
            String name,
            User creator) {

        this.name       = name;
        this.creator    = creator;
        this.created_at = Calendar.getInstance();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Getter & Setter
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public String getName() {
        return name;
    }

    public boolean isTasksBrowse() {
        return TasksBrowse;
    }

    public void setTasksBrowse(boolean tasksBrowse) {
        TasksBrowse = tasksBrowse;
    }

    public boolean isTasksCreate() {
        return TasksCreate;
    }

    public void setTasksCreate(boolean tasksCreate) {
        TasksCreate = tasksCreate;
    }

    public boolean isTasksAdmin() {
        return TasksAdmin;
    }

    public void setTasksAdmin(boolean tasksAdmin) {
        TasksAdmin = tasksAdmin;
    }

    public boolean isRolesAdmin() {
        return RolesAdmin;
    }

    public void setRolesAdmin(boolean rolesAdmin) {
        RolesAdmin = rolesAdmin;
    }

    public boolean isUserAdmin() {
        return UserAdmin;
    }

    public void setUserAdmin(boolean userAdmin) {
        UserAdmin = userAdmin;
    }

    public boolean isHomeworkCreate() {
        return HomeworkCreate;
    }

    public void setHomeworkCreate(boolean homeworkCreate) {
        HomeworkCreate = homeworkCreate;
    }

    public boolean isHomeworkBrowse() {
        return HomeworkBrowse;
    }

    public void setHomeworkBrowse(boolean homeworkBrowse) {
        HomeworkBrowse = homeworkBrowse;
    }

    public Role getFollowingRole() {
        return followingRole;
    }

    public void setFollowingRole(Role followingRole) {
        this.followingRole = followingRole;
    }

    public User getCreator() {
        return creator;
    }

    public Calendar getCreated_at() {
        return created_at;
    }

    public Calendar getUpdated_at() {
        return updated_at;
    }

    public User getUpdated_from() {
        return updated_from;
    }

    public void setUpdated_from(User updated_from) {
        this.updated_from = updated_from;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Ebean Overrides
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void save() {
        this.updated_at = Calendar.getInstance();
        super.save();
    }

    @Override
    public void update() {
        this.updated_at = Calendar.getInstance();
        super.update();
    }

    ca
}
