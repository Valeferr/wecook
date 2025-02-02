import { Expose } from 'class-transformer';

export enum MeasurementUnits {
  CENTILITER = "cl",
  MILLILITER = "ml",
  LITER = "l",

  MILLIGRAM = "mg",
  GRAM = "g",
  KILOGRAM = "kg",

  QUANTITY = "qty",
  TEASPOON = "teaspoon",
  TABLESPOON = "tablespoon",
  CUP = "cup",
  PINCH = "pinch",
  DASH = "dash",
  TIN = "tin",
}

export class RecipeIngredient {
  @Expose()
  public id?: number;

  @Expose()
  public quantity?: number;

  @Expose()
  public measurementUnit?: MeasurementUnits;

  @Expose()
  public ingredientId?: number;

  constructor(
    id?: number,
    quantity?: number,
    measurementUnit?: MeasurementUnits,
    ingredientId?: number
  ) {
    this.id = id;
    this.quantity = quantity;
    this.measurementUnit = measurementUnit;
    this.ingredientId = ingredientId;
  }
}