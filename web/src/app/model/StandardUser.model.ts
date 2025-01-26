import { Expose, Type } from 'class-transformer';
import { Roles, User } from './User.model';

export class StandardUser extends User {
  @Expose()
  @Type(() => String)
  public allergies: Array<string>;

  @Expose()
  public foodPreference: string;

  @Expose()
  public favoriteDish: string;

  constructor (
    id: number,
    email: string,
    username: string,
    password: string,
    role: Roles,
    allergies: Array<string>,
    foodPreference: string,
    favoriteDish: string
  ) {
    super(id, email, username, password, role);
    this.allergies = allergies;
    this.foodPreference = foodPreference;
    this.favoriteDish = favoriteDish;
  }
}