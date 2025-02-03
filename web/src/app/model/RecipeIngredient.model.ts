import { Expose } from 'class-transformer';
import { Ingredient } from './Ingredient.model';

export enum MeasurementUnits {
  CENTILITER = "CENTILITER",
  MILLILITER = "MILLILITER",
  LITER = "LITER",

  MILLIGRAM = "MILLIGRAM",
  GRAM = "GRAM",
  KILOGRAM = "KILOGRAM",

  QUANTITY = "QUANTITY",
  TEASPOON = "TEASPOON",
  TABLESPOON = "TABLESPOON",
  CUP = "CUP",
  PINCH = "PINCH",
  DASH = "DASH",
  TIN = "TIN",
}

export class RecipeIngredient {
  @Expose()
  public id: number;

  @Expose()
  public quantity: number;

  @Expose()
  public measurementUnit: MeasurementUnits;

  @Expose()
  public ingredient: Ingredient;

  constructor(
    id: number,
    quantity: number,
    measurementUnit: MeasurementUnits,
    ingredient: Ingredient
  ) {
    this.id = id;
    this.quantity = quantity;
    this.measurementUnit = measurementUnit;
    this.ingredient = ingredient;
  }
}