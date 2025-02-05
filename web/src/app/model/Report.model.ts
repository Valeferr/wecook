import { Expose, Type } from 'class-transformer';
import { StandardUser } from './StandardUser.model';

export enum Types {
  POST,
  COMMENT
}

export enum Reasons {
  SPAM,
  HATE_SPEECH,
  HARASSMENT,
  MISINFORMATION,
  VIOLENCE,
  NUDITY,
  COPYRIGHT_VIOLATION,
  ILLEGAL_CONTENT,
  OFFENSIVE_LANGUAGE,
  OTHER
}

export enum States {
  OPEN,
  IN_PROGRESS,
  CLOSED
}

export class Report {
  @Expose()
  public id: number;

  @Expose()
  public publicationDate: string;

  @Expose()
  public type: Types;

  @Expose()
  public reason: Reasons;

  @Expose()
  public status: States;

  @Expose()
  public user: StandardUser;

  constructor (
    id: number,
    publicationDate: string,
    type: Types,
    reason: Reasons,
    status: States,
    user: StandardUser
  ) {
    this.id = id;
    this.publicationDate = publicationDate;
    this.type = type;
    this.reason = reason;
    this.status = status;
    this.user = user;
  }
}