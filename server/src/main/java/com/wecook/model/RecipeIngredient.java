package com.wecook.model;

import com.google.gson.annotations.Expose;
import com.wecook.model.enums.MeasurementUnits;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

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
    @Expose
    private MeasurementUnits measurementUnit;

    @ManyToOne
    @JoinColumn(name = "step", nullable = false)
    private Step step;

    @ManyToOne
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
