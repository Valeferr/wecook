package com.wecook.model;

import com.google.gson.annotations.Expose;
import com.wecook.model.enums.FoodTypes;
import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
@org.hibernate.annotations.NamedQueries ({
        @org.hibernate.annotations.NamedQuery (
                name = Ingredient.GET_ALL,
                query = "From Ingredient"
        )
})
public class Ingredient {
    public static final String GET_ALL = "Ingredients_All";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "name")
    @Expose
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Expose
    private FoodTypes type;

    @OneToOne(mappedBy = "ingredient")
    private RecipeIngredient recipeIngredient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodTypes getType() {
        return type;
    }

    public void setType(FoodTypes type) {
        this.type = type;
    }

    public RecipeIngredient getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }
}
