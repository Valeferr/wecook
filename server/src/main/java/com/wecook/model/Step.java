package com.wecook.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "step")
public class Step  {
    public enum Actions {
        MIX,
        CUT,
        SLICE,
        GRATE,
        BLEND,
        KNEAD,
        COOK,
        BOIL,
        FRY,
        ROAST,
        SEASON,
        SERVE,
        MARINATE,
        PEEL,
        CHOP,
        DRAIN,
        GARNISH,
        MELT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    private long id;

    @Column(name = "description")
    @Expose
    private String description;

    @Column(name = "duration")
    @Expose
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    @Expose
    private Actions action;

    @Column(name = "step_index")
    @Expose
    private int stepIndex;

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
