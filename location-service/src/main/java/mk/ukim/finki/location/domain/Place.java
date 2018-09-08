package mk.ukim.finki.location.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Place.
 */
@Entity
@Table(name = "place")
public class Place implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "pos_x", precision = 10, scale = 2)
    private BigDecimal posX;

    @Column(name = "pos_y", precision = 10, scale = 2)
    private BigDecimal posY;

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

    public Place name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public Place addressLine(String addressLine) {
        this.addressLine = addressLine;
        return this;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public BigDecimal getPosX() {
        return posX;
    }

    public Place posX(BigDecimal posX) {
        this.posX = posX;
        return this;
    }

    public void setPosX(BigDecimal posX) {
        this.posX = posX;
    }

    public BigDecimal getPosY() {
        return posY;
    }

    public Place posY(BigDecimal posY) {
        this.posY = posY;
        return this;
    }

    public void setPosY(BigDecimal posY) {
        this.posY = posY;
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
        Place place = (Place) o;
        if (place.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), place.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Place{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", addressLine='" + getAddressLine() + "'" +
            ", posX=" + getPosX() +
            ", posY=" + getPosY() +
            "}";
    }
}
