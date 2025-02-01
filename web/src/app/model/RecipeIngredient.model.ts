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
  public id: string;

  @Expose()
  public quantity: number;

  @Expose()
  public measurementUnit: MeasurementUnits;

  constructor(
    id: string,
    quantity: number,
    measurementUnit: MeasurementUnits
  ) {
    this.id = id;
    this.quantity = quantity;
    this.measurementUnit = measurementUnit;
  }
}