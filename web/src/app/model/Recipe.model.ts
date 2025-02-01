export class Recipe {
  public id?: string;
  public title?: string;
  public description?: string;
  public category?: string;
  public difficulty?: string;
  
  constructor(
    id?: string,
    title?: string,
    description?: string,
    category?: string,
    difficulty?: string
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.category = category;
    this.difficulty = difficulty;
  }
}