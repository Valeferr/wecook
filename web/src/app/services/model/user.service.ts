import { StandardUser } from './../../model/StandardUser.model';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { User } from '../../model/User.model';
import { plainToInstance } from 'class-transformer';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly URL: string = 'http://localhost:8080/wecook/users';

  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post (
    user: User
  ): Observable<User> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<User>(this.URL, user, { headers: headers })
    .pipe(tap((response) => plainToInstance(User, response)),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  public searchByName (
    username: string
  ): Observable<Array<User>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
    
    return this.http.post<Array<User>>(`${this.URL}/searchByName`, username ).pipe(
      map((response) => response.map((user) => plainToInstance(User, user)))
    );
  }

  public getOne (
    userId: number
  ): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<StandardUser>(`${this.URL}/${userId}`, { headers: headers })
    .pipe(tap((user: StandardUser) => user = user), 
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    )
  }

  public getAll(): Observable<Array<User>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<User>>(this.URL, { headers: headers }).pipe
    (map((response) => response.map((user) => plainToInstance(User, user)))
    )
  }

  public patch (
    user: StandardUser
  ): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<StandardUser>(`${this.URL}/${user.id}`, user, { headers: headers })
      .pipe(tap((user: StandardUser) => user = user),
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error);
        })
      )
  }

  public delete (
    userId: number
  ): Observable<void> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<void>(`${this.URL}/${userId}`, { headers: headers });
  }

  //TODO Cambiare non va bene
  public putAllergies (
    userId: number,
    data: {
      allergies: Array<string>,
    }
  ): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<StandardUser>(`${this.URL}/${userId}/allergies`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(StandardUser, response))
    );
  }

  //TODO Cambiare non va bene
  public followUser (
    userId: number,
  ): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<StandardUser>(`${this.URL}/${userId}/follow`, { headers: headers }).pipe
    (tap((user: StandardUser) => user = user),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    )
  }

  //TODO Cambiare non va bene
  public unFollowUser(
    userId: number,
    followedId: number
  ): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<StandardUser>(`${this.URL}/${userId}/unfollow/${followedId}`).pipe
    (tap((user: StandardUser) => user = user),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    )
  }
}
