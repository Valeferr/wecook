import { Roles, User } from "./User.model";

export class ModeratorUser extends User {
    constructor (
      id: number,
      email: string,
      username: string,
      password: string,
      role: Roles,
      picture: string
    ) {
      super(id, email, username, password, role, picture);
    }
}