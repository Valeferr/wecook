import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable, ModelFunction } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { Roles, User } from '../model/User.model';
import { StandardUser } from '../model/StandardUser.model';
import { ModeratorUser } from '../model/ModeratorUser.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl: string = 'http://localhost:8080/wecook';

  private http = inject(HttpClient);
  private router = inject(Router);

  private user: User | null = null;

  constructor() { }

  public register(userData: {
    email: string,
    username: string,
    password: string,
    role: Roles
  }): Observable<StandardUser> {
    return this.http.post<StandardUser>(`${this.apiUrl}/users`, userData, { withCredentials: true });
  }

  public login(credentials: {email: string, password: string}): Observable<User> {
    return this.http.post<StandardUser | ModeratorUser>(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
      tap((user: StandardUser | ModeratorUser) => this.user = user),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  public logout(): void {
    this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).subscribe(() => {
      this.user = null;
      this.router.navigate(['/login']);
    })
  }

  public getRole(): Roles | null {
    return this.user ? this.user.role : null;
  }

  public isLogged(): boolean {
    return this.user !== null;
  }
}
