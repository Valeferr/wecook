import { Expose, Type } from 'class-transformer';
import { Step } from "./Step.model";

export class Recipe {
  @Expose()
  public id: number;

  @Expose()
  public title: string;

  @Expose()
  public description: string;

  @Expose()
  public difficulty: string;

  @Expose()
  public category: string;

  @Expose()
  public steps: Array<Step>;
  
  constructor(
    id: number,
    title: string,
    description: string,
    category: string,
    difficulty: string,
    steps: Array<Step> = new Array<Step>()
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.category = category;
    this.difficulty = difficulty;
    this.steps = steps;
  }
}