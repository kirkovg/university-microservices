package mk.ukim.finki.communication.service.dto;

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
 * Criteria class for the Message entity. This class is used in MessageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter content;

    private ZonedDateTimeFilter sentAt;

    private LongFilter fromId;

    private LongFilter toId;

    public MessageCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public ZonedDateTimeFilter getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTimeFilter sentAt) {
        this.sentAt = sentAt;
    }

    public LongFilter getFromId() {
        return fromId;
    }

    public void setFromId(LongFilter fromId) {
        this.fromId = fromId;
    }

    public LongFilter getToId() {
        return toId;
    }

    public void setToId(LongFilter toId) {
        this.toId = toId;
    }

    @Override
    public String toString() {
        return "MessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (sentAt != null ? "sentAt=" + sentAt + ", " : "") +
                (fromId != null ? "fromId=" + fromId + ", " : "") +
                (toId != null ? "toId=" + toId + ", " : "") +
            "}";
    }

}
