package mk.ukim.finki.career.service.dto;

import java.io.Serializable;
import mk.ukim.finki.career.domain.enumeration.JobType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Post entity. This class is used in PostResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /posts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostCriteria implements Serializable {
    /**
     * Class for filtering JobType
     */
    public static class JobTypeFilter extends Filter<JobType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private LocalDateFilter creationDate;

    private LocalDateFilter dueDate;

    private JobTypeFilter jobType;

    private StringFilter careerType;

    public PostCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateFilter creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public JobTypeFilter getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeFilter jobType) {
        this.jobType = jobType;
    }

    public StringFilter getCareerType() {
        return careerType;
    }

    public void setCareerType(StringFilter careerType) {
        this.careerType = careerType;
    }

    @Override
    public String toString() {
        return "PostCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
                (jobType != null ? "jobType=" + jobType + ", " : "") +
                (careerType != null ? "careerType=" + careerType + ", " : "") +
            "}";
    }

}
