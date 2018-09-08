package mk.ukim.finki.gradespreview.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A CompletedCourse.
 */
@Entity
@Table(name = "completed_course")
public class CompletedCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "grade", precision = 10, scale = 2)
    private BigDecimal grade;

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

    public CompletedCourse name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public CompletedCourse grade(BigDecimal grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
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
        CompletedCourse completedCourse = (CompletedCourse) o;
        if (completedCourse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), completedCourse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompletedCourse{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", grade=" + getGrade() +
            "}";
    }
}
