package mk.ukim.finki.publictransport.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Line.
 */
@Entity
@Table(name = "line")
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "university_stop_time")
    private ZonedDateTime universityStopTime;

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

    public Line name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getUniversityStopTime() {
        return universityStopTime;
    }

    public Line universityStopTime(ZonedDateTime universityStopTime) {
        this.universityStopTime = universityStopTime;
        return this;
    }

    public void setUniversityStopTime(ZonedDateTime universityStopTime) {
        this.universityStopTime = universityStopTime;
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
        Line line = (Line) o;
        if (line.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), line.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Line{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", universityStopTime='" + getUniversityStopTime() + "'" +
            "}";
    }
}
