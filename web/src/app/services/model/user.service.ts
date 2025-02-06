import { StandardUser } from './../../model/StandardUser.model';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { User } from '../../model/User.model';
import { plainToInstance } from 'class-transformer';
import { AuthService } from '../auth.service';
import { ModeratorUser } from '../../model/ModeratorUser.model';

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

    return this.http.post<User>(this.URL, user, { headers: headers }).pipe(
      map((response) => plainToInstance(User, response))
    );
  }

  public get<T extends User>(
    userId: number
  ): Observable<T> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<T>(`${this.URL}/${userId}`, { headers: headers }).pipe(
      map((response) => plainToInstance(User, response) as T)
    );
  }

  public getAll<T extends User>(): Observable<Array<T>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
  
    return this.http.get<Array<T>>(this.URL, { headers: headers }).pipe(
      map((response) => response.map((u) => plainToInstance(User, u) as T))
    );
  }  

  public patch<T extends User>(user: T): Observable<T> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
  
    return this.http.patch<T>(`${this.URL}/${user.id}`, user, { headers: headers }).pipe(
      map((response) => plainToInstance(User, response) as T)
    );
  }
  
  public delete(userId: number): Observable<void> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
  
    return this.http.delete<void>(`${this.URL}/${userId}`, { headers: headers });
  }

  public searchByUsername (
    username: string
  ): Observable<Array<StandardUser>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
    
    return this.http.post<Array<StandardUser>>(`${this.URL}/searchByUsername`, username ).pipe(
      map((response) => response.map((user) => plainToInstance(StandardUser, user)))
    );
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

  public followUser (
    data: {
      followedId: number,
    }
  ): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<StandardUser>(`${this.URL}/follower`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(StandardUser, response))
    );
  }

  public unfollowUser(
    userId: number,
  ): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<StandardUser>(`${this.URL}/follower/${userId}`, { headers: headers });
  }
}
