package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment {
    public enum States {
        ACTIVE,
        DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @Column(name = "publication_date")
    @Expose
    private LocalDate publicationDate;

    @Column(name = "text")
    @Expose
    private String text;

    @Column(name = "content_state")
    @Expose
    private States contentStatus;

    @ManyToOne
    @JoinColumn(name = "standard_user", nullable = false)
    private StandardUser standardUser;

    @ManyToOne
    @JoinColumn(name = "post")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CommentReport> commentReports = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public States getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(States contentStatus) {
        this.contentStatus = contentStatus;
    }

    public StandardUser getStandardUser() {
        return standardUser;
    }

    public void setStandardUser(StandardUser standardUser) {
        this.standardUser = standardUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Set<CommentReport> getCommentReports() {
        return commentReports;
    }

    public void setCommentReports(Set<CommentReport> commentReports) {
        this.commentReports = commentReports;
    }
}
