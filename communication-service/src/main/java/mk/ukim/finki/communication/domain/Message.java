package mk.ukim.finki.communication.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "sent_at")
    private ZonedDateTime sentAt;

    @OneToOne
    @JoinColumn(unique = true)
    private Appuser from;

    @OneToOne
    @JoinColumn(unique = true)
    private Appuser to;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Message content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public Message sentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
        return this;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Appuser getFrom() {
        return from;
    }

    public Message from(Appuser appuser) {
        this.from = appuser;
        return this;
    }

    public void setFrom(Appuser appuser) {
        this.from = appuser;
    }

    public Appuser getTo() {
        return to;
    }

    public Message to(Appuser appuser) {
        this.to = appuser;
        return this;
    }

    public void setTo(Appuser appuser) {
        this.to = appuser;
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
        Message message = (Message) o;
        if (message.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            "}";
    }
}
