package com.wecook.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "saved_posts")
public class SavedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "save_date", nullable = false)
    private LocalDate saveDate;

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

    public LocalDate getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDate saveDate) {
        this.saveDate = saveDate;
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
