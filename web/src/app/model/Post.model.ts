import { Expose } from "class-transformer";

export class Post {
  @Expose()
  public id: number;

  @Expose()
  public userId: number;

  @Expose()
  public userUsername: string;

  @Expose()
  public userPicture: string;

  @Expose()
  public recipeId: number;

  @Expose()
  public description: string;

  @Expose()
  public picture: string;

  @Expose()
  public saved: boolean;

  @Expose()
  public liked: boolean;

  @Expose()
  public likes: number;

  constructor(
    id: number,
    userId: number,
    userUsername: string,
    userPicture: string,
    recipeId: number,
    description: string,
    picture: string,
    saved: boolean,
    liked: boolean,
    likes: number
  ) {
    this.id = id;
    this.userId = userId;
    this.userUsername = userUsername;
    this.userPicture = userPicture;
    this.recipeId = recipeId;
    this.description = description;
    this.picture = picture;
    this.saved = saved;
    this.liked = liked;
    this.likes = likes;
  }
}