package com.wecook.model;

import com.google.gson.annotations.SerializedName;
import com.wecook.model.enums.FoodCategories;
import jakarta.persistence.*;
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

    @Transient
    private long followersAmount;

    @Transient
    private long followedAmount;

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

    public long getFollowersAmount() {
        return followersAmount;
    }

    public void setFollowersAmount(long followersAmount) {
        this.followersAmount = followersAmount;
    }

    public long getFollowedAmount() {
        return followedAmount;
    }

    public void setFollowedAmount(long followedAmount) {
        this.followedAmount = followedAmount;
    }
}
