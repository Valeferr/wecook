package com.wecook.model;

import com.google.gson.annotations.Expose;
import com.wecook.model.enums.FoodCategories;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "standard_users")
public class StandardUser extends User {
    public enum Allergy {
        MILK,
        EGGS,
        PEANUTS,
        TREE_NUTS,
        SOY,
        WHEAT_AND_GLUTEN,
        FISH,
        SHELLFISH,
        OTHER
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_allergies", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "allergy")
    @Expose
    private Set<Allergy> allergies;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_preference")
    @Expose
    private FoodCategories foodPreference;

    @Column(name = "favorite_dish")
    @Expose
    private String favoriteDish;

    @OneToMany(mappedBy = "standardUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "standardUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SavedPost> savedPosts = new HashSet<>();

    @OneToMany(mappedBy = "standardUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_follows",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private Set<StandardUser> following = new HashSet<>();

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    private Set<StandardUser> followers = new HashSet<>();

    public Set<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(Set<Allergy> allergies) {
        this.allergies = allergies;
    }

    public FoodCategories getFoodPreference() {
        return foodPreference;
    }

    public void setFoodPreference(FoodCategories foodPreference) {
        this.foodPreference = foodPreference;
    }

    public String getFavoriteDish() {
        return favoriteDish;
    }

    public void setFavoriteDish(String favoriteDish) {
        this.favoriteDish = favoriteDish;
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

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<StandardUser> getFollowing() {
        return following;
    }

    public void setFollowing(Set<StandardUser> following) {
        this.following = following;
    }

    public Set<StandardUser> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<StandardUser> followers) {
        this.followers = followers;
    }
}
