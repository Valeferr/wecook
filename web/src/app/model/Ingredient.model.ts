import { Expose } from 'class-transformer';

export class Ingredient {
  @Expose()
  public id: number;

  @Expose()
  public name: string;

  @Expose()
  public type: string;

  @Expose()
  public measurementUnits: Array<string>;

  constructor(
    id: number,
    name: string,
    type: string,
    measurementUnits: Array<string>
  ) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.measurementUnits = measurementUnits;
  }
}