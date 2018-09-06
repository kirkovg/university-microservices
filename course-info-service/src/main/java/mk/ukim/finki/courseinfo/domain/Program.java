package mk.ukim.finki.courseinfo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "program")
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Program name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Program description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Program courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Program addCourse(Course course) {
        this.courses.add(course);
        course.setProgram(this);
        return this;
    }

    public Program removeCourse(Course course) {
        this.courses.remove(course);
        course.setProgram(null);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Program program = (Program) o;
        if (program.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), program.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
