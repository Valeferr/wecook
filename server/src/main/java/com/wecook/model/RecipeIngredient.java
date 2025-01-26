package com.wecook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "recipe_ingredients")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = RecipeIngredient.GET_ALL_STEP_RECIPE_INGREDIENTS,
                query = "From RecipeIngredient As r Where r.step = :step"
        ),
        @org.hibernate.annotations.NamedQuery(
                name =RecipeIngredient.GET_ONE_STEP_RECIPE_INGREDIENTS,
                query = "From RecipeIngredient As r Where r.id = :id AND r.step = :step"
        )
})
public class RecipeIngredient {
    public enum MeasurementUnits {
        CENTILITER,
        MILLILITER,
        LITER,

        MILLIGRAM,
        GRAM,
        KILOGRAM,

        QUANTITY,
        TEASPOON,
        TABLESPOON,
        CUP,
        PINCH,
        DASH,
        TIN,
    }
    public static final String GET_ALL_STEP_RECIPE_INGREDIENTS = "RecipeIngredient_All";
    public static final String GET_ONE_STEP_RECIPE_INGREDIENTS = "RecipeIngredient_One";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @PositiveOrZero
    @Column(name = "quantity", nullable = false)
    @Expose
    private double quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    @SerializedName("unit")
    @Expose
    private MeasurementUnits measurementUnit;

    @ManyToOne
    @JoinColumn(name = "step", nullable = false)
    @Expose(serialize = false, deserialize = true)
    private Step step;

    @OneToOne
    @JoinColumn(name = "ingredient", nullable = false)
    @Expose(serialize = false, deserialize = true)
    private Ingredient ingredient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PositiveOrZero
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(@PositiveOrZero double quantity) {
        this.quantity = quantity;
    }

    public MeasurementUnits getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(MeasurementUnits measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
