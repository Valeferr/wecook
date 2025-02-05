package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "saved_posts")
public class SavedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "save_date", nullable = false)
    @Expose
    private LocalDate saveDate;

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
