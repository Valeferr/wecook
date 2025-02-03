import { Expose } from 'class-transformer';
import { RecipeIngredient } from './RecipeIngredient.model';

export class Step {
  @Expose()
  public id: number;

  @Expose()
  public description: string;

  @Expose()
  public duration: number;

  @Expose()
  public action: string;

  @Expose()
  public stepIndex: number;

  @Expose()
  public ingredients: Array<RecipeIngredient>;

  constructor(
    stepIndex: number,
    id: number,
    description: string,
    duration: number,
    action: string,
    ingredients: Array<RecipeIngredient> = new Array<RecipeIngredient>()
  ) {
    this.id = id;
    this.description = description;
    this.duration = duration;
    this.action = action;
    this.stepIndex = stepIndex;
    this.ingredients = ingredients;
  }
}