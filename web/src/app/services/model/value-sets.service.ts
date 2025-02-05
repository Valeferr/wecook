import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class ValueSetsService {
  private readonly URL: string = 'http://localhost:8080/wecook/valueSets';
  
  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() { }

  get difficulties() {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<string>>(`${this.URL}/difficulties`, { headers: headers });
  }

  get actions() {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<string>>(`${this.URL}/actions`, { headers: headers });
  }

  get measurementUnits() {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<string>>(`${this.URL}/measurementUnits`, { headers: headers });
  }

  get allergies() {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<string>>(`${this.URL}/allergies`, { headers: headers });
  }

  get foodCategories() {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<string>>(`${this.URL}/foodCategories`, { headers: headers });
  }

  get foodTypes() {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<string>>(`${this.URL}/foodTypes`, { headers: headers });
  }

  get reasons() {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
    
    return this.http.get<Array<string>>(`${this.URL}/reasons`, { headers: headers });
  }
}
