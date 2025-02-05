package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "reports")
@org.hibernate.annotations.NamedQueries(
        @org.hibernate.annotations.NamedQuery(
                name = Report.GET_ALL,
                query = "From Report"
        )
)
public class Report {
    public enum Types {
        POST,
        COMMENT
    }

    public enum Reasons {
        SPAM,
        HATE_SPEECH,
        HARASSMENT,
        MISINFORMATION,
        VIOLENCE,
        NUDITY,
        COPYRIGHT_VIOLATION,
        ILLEGAL_CONTENT,
        OFFENSIVE_LANGUAGE,
        OTHER
    }

    public enum States {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }

    public static final String GET_ALL = "Report_All";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "date", nullable = false)
    @Expose
    private LocalDate date;

    @Column(name = "type", nullable = false)
    @Expose
    private Types contentType;

    @Enumerated(EnumType.STRING) 
    @Column(name = "reason", nullable = false)
    @Expose
    private Reasons reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Expose
    private States status = States.OPEN;

    @ManyToOne
    @JoinColumn(name = "user")
    private StandardUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Types getContentType() {
        return contentType;
    }

    public void setContentType(Types contentType) {
        this.contentType = contentType;
    }

    public Reasons getReason() {
        return reason;
    }

    public void setReason(Reasons reason) {
        this.reason = reason;
    }

    public States getStatus() {
        return status;
    }

    public void setStatus(States status) {
        this.status = status;
    }

    public StandardUser getUser() {
        return user;
    }

    public void setUser(StandardUser user) {
        this.user = user;
    }
}
