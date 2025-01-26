package com.wecook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "step")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = Step.GET_ALL_RECIPE_STEPS,
                query = "From Step As s Where s.recipe = :recipe"
        ),
        @org.hibernate.annotations.NamedQuery(
                name = Step.GET_ONE_RECIPE_STEP,
                query = "From Step As s Where s.id = :id AND s.recipe = :recipe"
        )

})
public class Step {
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

    public static final String GET_ALL_RECIPE_STEPS = "All_Step_By_Recipe";
    public static final String GET_ONE_RECIPE_STEP = "One_Step_By_Recipe";
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
    @Column(name ="action")
    @Expose
    private Actions action;

    @Column(name = "step_index")
    @SerializedName("step_index")
    @Expose
    private int stepIndex;

    @ManyToOne
    @JoinColumn(name = "recipe")
    @Expose(serialize = false, deserialize = true)
    private Recipe recipe;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Expose(serialize = false, deserialize = true)
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

    public void setStepIndex(int index) {
        this.stepIndex = index;
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

    public void addIngredient(RecipeIngredient recipeIngredient) {
        this.ingredients.add(recipeIngredient);
    }

    public void removeIngredient(RecipeIngredient recipeIngredient) {
        this.ingredients.remove(recipeIngredient);
    }

}
