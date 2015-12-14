package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "group")
public class Group extends Model {
    @Id
    private long id;

    private String name;


    @ManyToOne
    private User tutor;

    @OneToMany
    private List<User> students;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Group(String name, User tutor) {
        this.name = name;
        this.tutor = tutor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getter & Setter
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public User getTutor() {
        return tutor;
    }

    @JsonProperty("tutor")
    public String getTutorName() {
        return this.tutor.getUsername();
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    @JsonIgnore
    public List<User> getStudents() {
        return students;
    }

    @JsonProperty("students")
    public int getStudentsCount() {
        return (this.students != null) ? this.students.size() : 0;
    }

    public void addStudent(User student) {
        if(this.students == null) {
            this.students = new ArrayList<>();
        }
        this.students.add(student);
    }
}
