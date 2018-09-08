package mk.ukim.finki.officehours.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import mk.ukim.finki.officehours.domain.enumeration.WeekDay;

/**
 * A Scheduledclass.
 */
@Entity
@Table(name = "scheduledclass")
public class Scheduledclass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "lecturer")
    private String lecturer;

    @Column(name = "location")
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private WeekDay day;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

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

    public Scheduledclass name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Scheduledclass description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLecturer() {
        return lecturer;
    }

    public Scheduledclass lecturer(String lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getLocation() {
        return location;
    }

    public Scheduledclass location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WeekDay getDay() {
        return day;
    }

    public Scheduledclass day(WeekDay day) {
        this.day = day;
        return this;
    }

    public void setDay(WeekDay day) {
        this.day = day;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Scheduledclass date(ZonedDateTime date) {
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
        Scheduledclass scheduledclass = (Scheduledclass) o;
        if (scheduledclass.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scheduledclass.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Scheduledclass{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", lecturer='" + getLecturer() + "'" +
            ", location='" + getLocation() + "'" +
            ", day='" + getDay() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
