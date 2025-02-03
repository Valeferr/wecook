import { Expose, Type } from 'class-transformer';
import { Step } from "./Step.model";

export enum Difficulties {
  EASY = "EASY",
  MEDIUM = "MEDIUM",
  HARD = "HARD"
}

export class Recipe {
  @Expose()
  public id: number;

  @Expose()
  public title: string;

  @Expose()
  public description: string;

  @Expose()
  public difficulty: Difficulties;

  @Expose()
  public category: string;

  @Expose()
  public steps: Array<Step>;
  
  constructor(
    id: number,
    title: string,
    description: string,
    category: string,
    difficulty: Difficulties,
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