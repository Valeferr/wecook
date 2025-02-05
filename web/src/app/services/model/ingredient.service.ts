import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Ingredient } from '../../model/Ingredient.model';
import { MeasurementUnits } from '../../model/RecipeIngredient.model';
import { map, Observable } from 'rxjs';
import { plainToInstance } from 'class-transformer';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {
  private readonly URL: string = 'http://localhost:8080/wecook/ingredient';

  private http = inject(HttpClient);

  constructor() { }

  public post(
    data: {
      name: string,
      type: string,
      measurementUnits: Array<MeasurementUnits>
    }
  ): Observable<Ingredient> {
    return this.http.post<Ingredient>(this.URL, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Ingredient, response))
    );
  }

  public get(
    ingredientId: number
  ): Observable<Ingredient> {
    return this.http.get<Ingredient>(`${this.URL}/${ingredientId}`, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Ingredient, response))
    );
  }

  public getAll(): Observable<Array<Ingredient>> {
    return this.http.get<Array<Ingredient>>(this.URL, { withCredentials: true }).pipe(
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
    return this.http.patch<Ingredient>(`${this.URL}/${ingredientId}`, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Ingredient, response))
    );
  }

  public delete(
    ingredientId: number
  ): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${ingredientId}`, { withCredentials: true });
  }
}
