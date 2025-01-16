package com.wecook.model;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName("allergies")
    private Set<Allergy> allergies;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_preference")
    @SerializedName("food_preference")
    private FoodCategories foodPreference;

    @Column(name = "favorite_dish")
    @SerializedName("favorite_dish")
    private String favoriteDish;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SavedPost> savedPosts = new HashSet<>();

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
}
