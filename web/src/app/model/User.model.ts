import { Expose } from 'class-transformer';

export enum Roles {
  Standard = "STANDARD",
  Moderator = "MODERATOR",
}

export class User {
  @Expose()
  public id: number;

  @Expose()
  public email: string;

  @Expose()
  public username: string;

  @Expose()
  public password: string;

  @Expose()
  public role: Roles;

  @Expose()
  public picture: string | null;

  constructor (
    id: number,
    email: string,
    username: string,
    password: string,
    role: Roles,
    picture: string | null
  ) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.password = password;
    this.role = role;
    this.picture = picture;
  }
}