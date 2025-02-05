import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Ingredient } from '../../model/Ingredient.model';
import { MeasurementUnits } from '../../model/RecipeIngredient.model';
import { map, Observable } from 'rxjs';
import { plainToInstance } from 'class-transformer';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  private readonly URL: string = 'http://localhost:8080/wecook/ingredient';

  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post(
    data: {
      name: string,
      type: string,
      measurementUnits: Array<MeasurementUnits>
    }
  ): Observable<Ingredient> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<Ingredient>(this.URL, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Ingredient, response))
    );
  }

  public get(
    ingredientId: number
  ): Observable<Ingredient> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Ingredient>(`${this.URL}/${ingredientId}`, { headers: headers }).pipe(
      map((response) => plainToInstance(Ingredient, response))
    );
  }

  public getAll(): Observable<Array<Ingredient>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<Ingredient>>(this.URL, { headers: headers }).pipe(
      map((response) => response.map((ingredient) => plainToInstance(Ingredient, ingredient)))
    );
  }

  public patch(
    ingredientId: number,
    data: Partial<{
      name: string,
      type: string,
      measurementUnits: Array<MeasurementUnits>
    }>
  ): Observable<Ingredient> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<Ingredient>(`${this.URL}/${ingredientId}`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Ingredient, response))
    );
  }

  public delete(
    ingredientId: number
  ): Observable<void> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<void>(`${this.URL}/${ingredientId}`, { headers: headers });
  }
}
