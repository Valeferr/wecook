package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private StandardUser standardUser;

    @ManyToOne
    @JoinColumn(name = "post", nullable = false)
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
