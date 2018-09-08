package mk.ukim.finki.assignedtask.service.dto;

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
 * Criteria class for the Task entity. This class is used in TaskResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tasks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter courseName;

    private StringFilter description;

    private ZonedDateTimeFilter dueDate;

    public TaskCriteria() {
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

    public ZonedDateTimeFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTimeFilter dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "TaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (courseName != null ? "courseName=" + courseName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            "}";
    }

}
