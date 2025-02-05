import { Expose } from 'class-transformer';
import { Roles, User } from './User.model';

export class StandardUser extends User {
  @Expose()
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
    picture: string,
    allergies: Array<string>,
    foodPreference: string,
    favoriteDish: string
  ) {
    super(id, email, username, password, role, picture);
    this.allergies = allergies;
    this.foodPreference = foodPreference;
    this.favoriteDish = favoriteDish;
  }
}