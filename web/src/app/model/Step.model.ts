import { Expose } from 'class-transformer';
import { RecipeIngredient } from './RecipeIngredient.model';

export enum Actions {
  MIX = "mix",
  CUT = "cut",
  SLICE = "slice",
  GRATE = "grate",
  BLEND = "blend",
  KNEAD = "knead",
  COOK = "cook",
  BOIL = "boil",
  FRY = "fry",
  ROAST = "roast",
  SEASON = "season",
  SERVE = "serve",
  MARINATE = "marinate",
  PEEL = "peel",
  CHOP = "chop",
  DRAIN = "drain",
  GARNISH = "garnish",
  MELT = "melt",
}

export class Step {
  @Expose()
  public id?: number;

  @Expose()
  public description?: Actions;

  @Expose()
  public duration?: number;

  @Expose()
  public action?: Actions;

  @Expose()
  public stepIndex: number;

  @Expose()
  public ingredients: Array<RecipeIngredient>;

  constructor(
    stepIndex: number,
    id?: number,
    description?: Actions,
    duration?: number,
    action?: Actions,
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