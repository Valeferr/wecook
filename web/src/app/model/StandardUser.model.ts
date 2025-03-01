import { Expose } from 'class-transformer';
import { Roles, User } from './User.model';

export class StandardUser extends User {
  @Expose()
  public allergies: Array<string>;

  @Expose()
  public foodPreference: string | null;

  @Expose()
  public favoriteDish: string | null;

  @Expose()
  public posts: number;

  @Expose()
  public followers: number;

  @Expose()
  public following: number;

  @Expose()
  public isFollowing: boolean;

  constructor (
    id: number,
    email: string,
    username: string,
    password: string,
    role: Roles,
    picture: string,
    allergies: Array<string>,
    foodPreference: string | null,
    favoriteDish: string | null,
    posts: number,
    followers: number,
    following: number,
    isFollowing: boolean
  ) {
    super(id, email, username, password, role, picture);
    this.allergies = allergies;
    this.foodPreference = foodPreference;
    this.favoriteDish = favoriteDish;
    this.posts = posts;
    this.followers = followers;
    this.following = following;
    this.isFollowing = isFollowing;
  }
}