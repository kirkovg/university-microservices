package mk.ukim.finki.officehours.service.dto;

import java.io.Serializable;
import mk.ukim.finki.officehours.domain.enumeration.WeekDay;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Scheduledclass entity. This class is used in ScheduledclassResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /scheduledclasses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ScheduledclassCriteria implements Serializable {
    /**
     * Class for filtering WeekDay
     */
    public static class WeekDayFilter extends Filter<WeekDay> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter lecturer;

    private StringFilter location;

    private WeekDayFilter day;

    private ZonedDateTimeFilter date;

    public ScheduledclassCriteria() {
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

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLecturer() {
        return lecturer;
    }

    public void setLecturer(StringFilter lecturer) {
        this.lecturer = lecturer;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public WeekDayFilter getDay() {
        return day;
    }

    public void setDay(WeekDayFilter day) {
        this.day = day;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ScheduledclassCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (lecturer != null ? "lecturer=" + lecturer + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (day != null ? "day=" + day + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
            "}";
    }

}
