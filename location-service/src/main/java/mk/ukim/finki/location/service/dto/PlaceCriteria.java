package mk.ukim.finki.location.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;





/**
 * Criteria class for the Place entity. This class is used in PlaceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /places?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlaceCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter addressLine;

    private BigDecimalFilter posX;

    private BigDecimalFilter posY;

    public PlaceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(StringFilter addressLine) {
        this.addressLine = addressLine;
    }

    public BigDecimalFilter getPosX() {
        return posX;
    }

    public void setPosX(BigDecimalFilter posX) {
        this.posX = posX;
    }

    public BigDecimalFilter getPosY() {
        return posY;
    }

    public void setPosY(BigDecimalFilter posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "PlaceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (addressLine != null ? "addressLine=" + addressLine + ", " : "") +
                (posX != null ? "posX=" + posX + ", " : "") +
                (posY != null ? "posY=" + posY + ", " : "") +
            "}";
    }

}
