package com.wecook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "recipe_ingredients")
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    @Column(name = "quantity", nullable = false)
    private double quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private MeasurementUnits measurementUnit;

    @ManyToOne
    @JoinColumn(name = "step", nullable = false)
    private Step step;

    @OneToOne
    @JoinColumn(name = "ingredient", nullable = false)
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
