import { StandardUser } from './../../model/StandardUser.model';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { User } from '../../model/User.model';
import { plainToInstance } from 'class-transformer';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly URL: string = 'http://localhost:8080/wecook/users';

  private http = inject(HttpClient);

  constructor() {

  }

  public post (
    user: User
  ): Observable<User> {
    return this.http.post<User>(this.URL, user, { withCredentials: true})
    .pipe(tap((response) => plainToInstance(User, response)),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  public getOne (
    userId: number
  ): Observable<StandardUser> {
    return this.http.get<StandardUser>(`${this.URL}/` + userId)
    .pipe(tap((user: StandardUser) => user = user), 
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    )
  }

  public getAll(): Observable<Array<User>> {
    return this.http.get<Array<User>>(this.URL, { withCredentials: true }).pipe
    (map((response) => response.map((user) => plainToInstance(User, user)))
    )
  }

  public patch (
    user: StandardUser
  ): Observable<StandardUser> {
    return this.http.patch<StandardUser>(`${this.URL}/${user.id}`, user)
      .pipe(tap((user: StandardUser) => user = user),
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error);
        })
      )
  } 

  //TODO: controllare meglio
  public putAllergies (
    userId: number,
    data: {
      allergies: Array<string>,
    }
  ): Observable<StandardUser> {
    return this.http.patch<StandardUser>(`${this.URL}/${userId}/allergies`, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(StandardUser, response))
    );
  }

  public followUser (
    userId: number,
  ): Observable<StandardUser> {
    return this.http.delete<StandardUser>(`${this.URL}/${userId}/follow`).pipe
    (tap((user: StandardUser) => user = user),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    )
  }

  public unFollowUser(
    userId: number,
    followedId: number
  ): Observable<StandardUser> {
    return this.http.delete<StandardUser>(`${this.URL}/${userId}/unfollow/${followedId}`).pipe
    (tap((user: StandardUser) => user = user),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    )
  }

  public delete (
    userId: number
  ): Observable<void> {
    return this.http.delete<void>(`${this.URL}/` + userId, { withCredentials: true });
  }

}
