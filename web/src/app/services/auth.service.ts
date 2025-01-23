import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/wecook';
  private isLogged = false;

  private http = inject(HttpClient);
  private router = inject(Router);

  constructor() { }

  public login(credentials: {username: string, password: string}): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
      tap(() => this.isLogged = true)
    )
  }

  public logout(credentials: {username: string, password: string}): void {
    this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).subscribe(() => {
      this.isLogged = false;
      this.router.navigate(['/login']);
    })
  }

  public isLoggedIn(): boolean {
    return this.isLogged;
  }
}
