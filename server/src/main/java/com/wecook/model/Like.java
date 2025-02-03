package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private StandardUser standardUser;

    @ManyToOne
    @JoinColumn(name = "post", nullable = false)
    private Post post;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
