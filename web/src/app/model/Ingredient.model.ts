import { Expose } from 'class-transformer';

export class Ingredient {
  @Expose()
  public id: string;

  @Expose()
  public name: string;

  @Expose()
  public foodType: string;

  constructor(
    id: string,
    name: string,
    foodType: string
  ) {
    this.id = id;
    this.name = name;
    this.foodType = foodType;
  }
}