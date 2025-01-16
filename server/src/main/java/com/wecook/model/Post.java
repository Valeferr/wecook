package com.wecook.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    public enum States {
        ACTIVE,
        DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "post_picture", nullable = false)
    private byte[] postPicture;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_state", nullable = false)
    private States status;

    @OneToOne
    @JoinColumn(name = "post", nullable = false)
    private Recipe recipe;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PostReport> postReports = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SavedPost> savedPosts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public byte[] getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(byte[] postPicture) {
        this.postPicture = postPicture;
    }

    public States getStatus() {
        return status;
    }

    public void setStatus(States status) {
        this.status = status;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<PostReport> getPostReports() {
        return postReports;
    }

    public void setPostReports(Set<PostReport> postReports) {
        this.postReports = postReports;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<SavedPost> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(Set<SavedPost> savedPosts) {
        this.savedPosts = savedPosts;
    }
}
