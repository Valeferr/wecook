package com.wecook.model;

import com.google.gson.annotations.Expose;
import com.wecook.model.enums.FoodCategories;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = Recipe.GET_ALL,
                query = "From Recipe"
        )
})
public class Recipe  {
    public enum Difficulties {
        EASY,
        MEDIUM,
        HARD
    }

    public static final String GET_ALL = "Recipe_All";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @Column(name = "title", nullable = false)
    @Expose
    private String title;

    @Column(name = "description", nullable = false)
    @Expose
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    @Expose
    private Difficulties difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    @Expose
    private FoodCategories category;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Step> steps = new HashSet<>();

    @OneToOne(mappedBy = "recipe")
    private Post post;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulties getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulties difficulty) {
        this.difficulty = difficulty;
    }

    public FoodCategories getCategory() {
        return category;
    }

    public void setCategory(FoodCategories category) {
        this.category = category;
    }

    public Set<Step> getSteps() {
        return steps;
    }

    public void setSteps(Set<Step> steps) {
        this.steps = steps;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
