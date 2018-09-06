package mk.ukim.finki.courseinfo.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "description")
    private String description;

    @Column(name = "professor")
    private String professor;

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

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredits() {
        return credits;
    }

    public Course credits(Integer credits) {
        this.credits = credits;
        return this;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfessor() {
        return professor;
    }

    public Course professor(String professor) {
        this.professor = professor;
        return this;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
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
        Course course = (Course) o;
        if (course.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", credits=" + getCredits() +
            ", description='" + getDescription() + "'" +
            ", professor='" + getProfessor() + "'" +
            "}";
    }
}
