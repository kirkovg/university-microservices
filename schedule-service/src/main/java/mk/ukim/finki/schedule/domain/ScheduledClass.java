package mk.ukim.finki.schedule.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ScheduledClass.
 */
@Entity
@Table(name = "scheduled_class")
public class ScheduledClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "description")
    private String description;

    @Column(name = "lecturer")
    private String lecturer;

    @Column(name = "location")
    private String location;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public ScheduledClass courseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public ScheduledClass description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLecturer() {
        return lecturer;
    }

    public ScheduledClass lecturer(String lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getLocation() {
        return location;
    }

    public ScheduledClass location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public ScheduledClass date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
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
        ScheduledClass scheduledClass = (ScheduledClass) o;
        if (scheduledClass.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scheduledClass.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ScheduledClass{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            ", description='" + getDescription() + "'" +
            ", lecturer='" + getLecturer() + "'" +
            ", location='" + getLocation() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
