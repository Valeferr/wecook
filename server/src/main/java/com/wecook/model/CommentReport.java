package com.wecook.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comment_reports")
public class CommentReport extends Report {
    @ManyToOne
    @JoinColumn(name = "comment")
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
