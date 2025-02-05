import { Expose } from 'class-transformer';
import { User } from './User.model';

export class Comment {
  @Expose()
  public id: number;

  @Expose()
  public publicationDate: string;

  @Expose()
  public text: string;

  @Expose()
  public status: Array<string>;

  @Expose()
  public user: User;

  constructor(
    id: number,
    publicationDate: string,
    text: string,
    status: Array<string>,
    user: User
  ) {
    this.id = id;
    this.publicationDate = publicationDate;
    this.text = text;
    this.status = status;
    this.user = user;
  }
}