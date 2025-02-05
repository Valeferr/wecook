import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { MeasurementUnits, RecipeIngredient } from '../../model/RecipeIngredient.model';
import { Ingredient } from '../../model/Ingredient.model';
import { map } from 'rxjs';
import { plainToInstance } from 'class-transformer';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class RecipeIngredientService {
  private readonly URL: string = 'http://localhost:8080/wecook/recipe/{recipeId}/step/{stepId}/recipeIngredient';
  
  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post(
    recipeId: number,
    stepId: number,
    data: {
      quantity: number,
      measurementUnit: MeasurementUnits,
      ingredientId: number
    }
  ) {
    const url = this.URL.replace("{recipeId}", String(recipeId)).replace("{stepId}", String(stepId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<RecipeIngredient>(url, data, { headers: headers }).pipe(
      map((response) => {
        const recipeIngredient = plainToInstance(RecipeIngredient, response);
        recipeIngredient.ingredient = plainToInstance(Ingredient, recipeIngredient.ingredient);
        return recipeIngredient;
      })
    );
  }

  public get(
    recipeId: number,
    stepId: number,
    recipeIngredientId: number
  ) {
    const url = this.URL.replace("{recipeId}", String(recipeId)).replace("{stepId}", String(stepId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<RecipeIngredient>(url + recipeIngredientId, { headers: headers }).pipe(
      map((response) => {
        const recipeIngredient = plainToInstance(RecipeIngredient, response);
        recipeIngredient.ingredient = plainToInstance(Ingredient, recipeIngredient.ingredient);
        return recipeIngredient;
      })
    );
  }

  public getAll(
    recipeId: number,
    stepId: number
  ) {
    const url = this.URL.replace("{recipeId}", String(recipeId)).replace("{stepId}", String(stepId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<RecipeIngredient>>(url, { headers: headers }).pipe(
      map((response) => response.map((i) => {
        const recipeIngredient = plainToInstance(RecipeIngredient, i);
        recipeIngredient.ingredient = plainToInstance(Ingredient, recipeIngredient.ingredient);
        return recipeIngredient;
      })
    ));
  }

  public patch(
    recipeId: number,
    stepId: number,
    recipeIngredientId: number,
    data: Partial<{
      quantity: number,
      measurementUnit: MeasurementUnits,
      ingredientId: number
    }>
  ) {
    const url = this.URL.replace("{recipeId}", String(recipeId)).replace("{stepId}", String(stepId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<RecipeIngredient>(url + recipeIngredientId, data, { headers: headers }).pipe(
      map((response) => {
        const recipeIngredient = plainToInstance(RecipeIngredient, response);
        recipeIngredient.ingredient = plainToInstance(Ingredient, recipeIngredient.ingredient);
        return recipeIngredient;
      })
    );
  }
}
