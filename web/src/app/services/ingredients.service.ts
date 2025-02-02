import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Ingredient } from '../model/Ingredient.model';

@Injectable({
  providedIn: 'root'
})
export class IngredientsService {
private apiUrl: string = 'http://localhost:8080/wecook/ingredients';

  private http = inject(HttpClient);
  
  private ingredients: Array<Ingredient> = [
    new Ingredient(1, 'Tomato', 'vegetable', ['qty', 'g', 'kg']),
    new Ingredient(2, 'Onion', 'vegetable', ['qty', 'g', 'kg']),
    new Ingredient(3, 'Garlic', 'vegetable', ['qty', 'g', 'kg']),
    new Ingredient(4, 'Carrot', 'vegetable', ['qty', 'g', 'kg']),
    new Ingredient(5, 'Potato', 'vegetable', ['qty', 'g', 'kg']),
    new Ingredient(6, 'Salt', 'condiment', ['g', 'kg']),
    new Ingredient(7, 'Pepper', 'condiment', ['g', 'kg']),
    new Ingredient(8, 'Sugar', 'condiment', ['g', 'kg']),
    new Ingredient(9, 'Flour', 'condiment', ['g', 'kg']),
    new Ingredient(10, 'Rice', 'cereal', ['g', 'kg']),
    new Ingredient(11, 'Pasta', 'cereal', ['g', 'kg']),
    new Ingredient(12, 'Bread', 'cereal', ['g', 'kg']),
    new Ingredient(13, 'Milk', 'dairy', ['ml', 'l']),
    new Ingredient(14, 'Butter', 'dairy', ['g', 'kg']),
    new Ingredient(15, 'Egg', 'dairy', ['qty']),
    new Ingredient(16, 'Cheese', 'dairy', ['g', 'kg']),
    new Ingredient(17, 'Chicken', 'meat', ['g', 'kg']),
    new Ingredient(18, 'Beef', 'meat', ['g', 'kg']),
    new Ingredient(19, 'Pork', 'meat', ['g', 'kg']),
    new Ingredient(20, 'Fish', 'meat', ['g', 'kg']),
  ]

  constructor() {}

  public getIngredients(): Array<Ingredient> {
    return this.ingredients;
  }

  public getIngredientById(id: number): Ingredient | undefined {
    return this.ingredients.find(ingredient => ingredient.id === id);
  }
}
