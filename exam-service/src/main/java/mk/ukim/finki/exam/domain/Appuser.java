package mk.ukim.finki.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Appuser.
 */
@Entity
@Table(name = "appuser")
public class Appuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "appuser")
    private Set<Exam> exams = new HashSet<>();

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

    public Appuser name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Appuser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Exam> getExams() {
        return exams;
    }

    public Appuser exams(Set<Exam> exams) {
        this.exams = exams;
        return this;
    }

    public Appuser addExam(Exam exam) {
        this.exams.add(exam);
        exam.setAppuser(this);
        return this;
    }

    public Appuser removeExam(Exam exam) {
        this.exams.remove(exam);
        exam.setAppuser(null);
        return this;
    }

    public void setExams(Set<Exam> exams) {
        this.exams = exams;
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
        Appuser appuser = (Appuser) o;
        if (appuser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appuser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Appuser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
