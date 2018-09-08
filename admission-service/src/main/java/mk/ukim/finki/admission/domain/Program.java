package mk.ukim.finki.admission.domain;

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

    @Column(name = "length_in_years")
    private Integer lengthInYears;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "program")
    private Set<Document> documents = new HashSet<>();

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

    public Integer getLengthInYears() {
        return lengthInYears;
    }

    public Program lengthInYears(Integer lengthInYears) {
        this.lengthInYears = lengthInYears;
        return this;
    }

    public void setLengthInYears(Integer lengthInYears) {
        this.lengthInYears = lengthInYears;
    }

    public String getTitle() {
        return title;
    }

    public Program title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public Program documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public Program addDocument(Document document) {
        this.documents.add(document);
        document.setProgram(this);
        return this;
    }

    public Program removeDocument(Document document) {
        this.documents.remove(document);
        document.setProgram(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
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
            ", lengthInYears=" + getLengthInYears() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
