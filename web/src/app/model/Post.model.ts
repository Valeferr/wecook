import { Expose } from "class-transformer";
import { Recipe } from "./Recipe.model";
import { User } from "./User.model";
import { Comment } from "./Comment.model";

export class Post {
  @Expose()
  public id: number;

  @Expose()
  public picture: string;

  @Expose()
  public saved: boolean;

  @Expose()
  public liked: boolean;

  @Expose()
  public likes: number;

  @Expose()
  public duration: number;

  @Expose()
  public recipe: Recipe;

  @Expose()
  public user: User;

  @Expose()
  public comments: Array<Comment>;

  constructor(
    id: number,
    picture: string,
    saved: boolean,
    liked: boolean,
    likes: number,
    duration: number,
    recipe: Recipe,
    user: User,
    comments: Array<Comment> = new Array<Comment>()
  ) {
    this.id = id;
    this.picture = picture;
    this.saved = saved;
    this.liked = liked;
    this.likes = likes;
    this.duration = duration;
    this.recipe = recipe;
    this.user = user;
    this.comments = comments;
  }
}