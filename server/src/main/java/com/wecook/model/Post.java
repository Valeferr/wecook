package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = Post.GET_ALL,
                query = "From Post"
        )
})
public class Post {
    public enum States {
        ACTIVE,
        DELETED
    }

    public static final String GET_ALL = "Post_All";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "publication_date")
    @Expose
    private LocalDate publicationDate;

    @Lob
    @Column(name = "post_picture", columnDefinition = "MEDIUMBLOB", nullable = false)
    @Expose
    private byte[] postPicture;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_state", nullable = false)
    @Expose
    private States status;

    @OneToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "standard_user")
    private StandardUser standardUser;

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

    public StandardUser getStandardUser() {
        return standardUser;
    }

    public void setStandardUser(StandardUser standardUser) {
        this.standardUser = standardUser;
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
