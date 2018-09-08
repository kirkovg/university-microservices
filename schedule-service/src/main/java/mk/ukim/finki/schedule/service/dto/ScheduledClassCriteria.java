package mk.ukim.finki.schedule.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the ScheduledClass entity. This class is used in ScheduledClassResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /scheduled-classes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ScheduledClassCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter courseName;

    private StringFilter description;

    private StringFilter lecturer;

    private StringFilter location;

    private ZonedDateTimeFilter date;

    public ScheduledClassCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCourseName() {
        return courseName;
    }

    public void setCourseName(StringFilter courseName) {
        this.courseName = courseName;
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

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ScheduledClassCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (courseName != null ? "courseName=" + courseName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (lecturer != null ? "lecturer=" + lecturer + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
            "}";
    }

}
