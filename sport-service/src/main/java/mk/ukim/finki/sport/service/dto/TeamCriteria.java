package mk.ukim.finki.sport.service.dto;

import java.io.Serializable;
import mk.ukim.finki.sport.domain.enumeration.SportType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Team entity. This class is used in TeamResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /teams?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeamCriteria implements Serializable {
    /**
     * Class for filtering SportType
     */
    public static class SportTypeFilter extends Filter<SportType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private SportTypeFilter sportType;

    private LongFilter playerId;

    public TeamCriteria() {
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

    public SportTypeFilter getSportType() {
        return sportType;
    }

    public void setSportType(SportTypeFilter sportType) {
        this.sportType = sportType;
    }

    public LongFilter getPlayerId() {
        return playerId;
    }

    public void setPlayerId(LongFilter playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "TeamCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (sportType != null ? "sportType=" + sportType + ", " : "") +
                (playerId != null ? "playerId=" + playerId + ", " : "") +
            "}";
    }

}
