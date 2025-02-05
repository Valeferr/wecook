import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
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
  private readonly apiUrl: string = 'http://localhost:8080/wecook/auth';

  private http = inject(HttpClient);
  private router = inject(Router);

  private user: User | null = null;
  private token: string | null = null;

  constructor() {}

  public register(userData: {
    email: string,
    username: string,
    password: string,
    role: Roles
  }): Observable<StandardUser> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.token}`
    });

    return this.http.post<StandardUser>(`${this.apiUrl}/users`, userData, { headers: headers });
  }

  public login(credentials: { email: string; password: string }): Observable<User> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.token}`
    });
    
    return this.http.post<User>(`${this.apiUrl}/login`, credentials, { headers: headers }).pipe(
      tap((user: any) => {
        this.user = user;
        this.token = user.token;
      }),
      catchError((error: HttpErrorResponse) => throwError(() => error))
    );
  }

  public logout(): void {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.token}`
    });

    this.http.post(`${this.apiUrl}/logout`, {}, { headers: headers }).subscribe(() => {
      this.user = null;
      this.token = null;
      this.router.navigate(['/login']);
    })
  }

  public getUser(): User | null {
    return this.user;
  }

  public getToken(): string | null {
    return this.token;
  }

  public isLogged(): boolean {
    return this.token !== null;
  }
}
