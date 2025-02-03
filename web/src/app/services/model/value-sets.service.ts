import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ValueSetsService {
  private readonly URL: string = 'http://localhost:8080/wecook/valueSets';
  
  private http = inject(HttpClient);

  constructor() { }

  get difficulties() {
    return this.http.get<Array<string>>(`${this.URL}/difficulties`, { withCredentials: true });
  }

  get actions() {
    return this.http.get<Array<string>>(`${this.URL}/actions`, { withCredentials: true });
  }

  get measurementUnits() {
    return this.http.get<Array<string>>(`${this.URL}/measurementUnits`, { withCredentials: true });
  }

  get allergies() {
    return this.http.get<Array<string>>(`${this.URL}/allergies`, { withCredentials: true });
  }

  get foodCategories() {
    return this.http.get<Array<string>>(`${this.URL}/foodCategories`, { withCredentials: true });
  }

  get foodTypes() {
    return this.http.get<Array<string>>(`${this.URL}/foodTypes`, { withCredentials: true });
  }

  get reasons() {
    return this.http.get<Array<string>>(`${this.URL}/reasons`, { withCredentials: true });
  }
}
