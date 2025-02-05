import { Expose } from "class-transformer";

export class SavedPost {
  @Expose()
  public id: number;

  @Expose()
  public savedDate: Date;

  constructor(
    id: number,
    savedDate: string | Date
  ) {
    this.id = id;

    if (savedDate instanceof Date) {
      this.savedDate = savedDate;
    } else {
      this.savedDate = new Date(savedDate);
    }
  }
}