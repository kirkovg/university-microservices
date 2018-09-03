package mk.ukim.finki.directory.service.dto;

import java.io.Serializable;
import mk.ukim.finki.directory.domain.enumeration.UniversityRole;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Contact entity. This class is used in ContactResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contacts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable {
    /**
     * Class for filtering UniversityRole
     */
    public static class UniversityRoleFilter extends Filter<UniversityRole> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private UniversityRoleFilter role;

    private StringFilter contactEmail;

    public ContactCriteria() {
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

    public UniversityRoleFilter getRole() {
        return role;
    }

    public void setRole(UniversityRoleFilter role) {
        this.role = role;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return "ContactCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (role != null ? "role=" + role + ", " : "") +
                (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
            "}";
    }

}
