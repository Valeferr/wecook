import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Recipe } from '../../model/Recipe.model';
import { map, Observable } from 'rxjs';
import { plainToInstance } from 'class-transformer';
import { Step } from '../../model/Step.model';
import { RecipeIngredient } from '../../model/RecipeIngredient.model';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private readonly URL: string = 'http://localhost:8080/wecook/recipe';
  
  private http = inject(HttpClient);

  constructor() { }

  public post(
    data: {
      title: string,
      description: string,
      difficulty: string,
      category: string
    }
  ): Observable<Recipe> {
    return this.http.post<Recipe>(this.URL, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Recipe, response))
    );
  }

  public get(id: number): Observable<Recipe> {
    return this.http.get<Recipe>(`${this.URL}/${id}`, { withCredentials: true }).pipe(
      map((response) => {
        const recipe = plainToInstance(Recipe, response);
        recipe.steps = recipe.steps.map((s) => {
          const step = plainToInstance(Step, s);
          step.ingredients = step.ingredients.map((i) => plainToInstance(RecipeIngredient, i));
          return step;
        });
        return recipe;
      })
    );
  }

  public getAll(): Observable<Array<Recipe>> {
    return this.http.get<Array<Recipe>>(this.URL, { withCredentials: true }).pipe(
      map((response) => response.map((r) => {
        const recipe = plainToInstance(Recipe, r);
        recipe.steps = recipe.steps.map((s) => {
          const step = plainToInstance(Step, s);
          step.ingredients = step.ingredients.map((i) => plainToInstance(RecipeIngredient, i));
          return step;
        });
        return recipe;
      })
    ));
  }

  public patch(
    id: number,
    data: Partial<{
      title: string,
      description: string,
      difficulty: string,
      category: string
    }>
  ): Observable<Recipe> {
    return this.http.patch<Recipe>(`${this.URL}/${id}`, data, { withCredentials: true }).pipe(
      map((response) => {
        const recipe = plainToInstance(Recipe, response);
        recipe.steps = recipe.steps.map((step) => plainToInstance(Step, step));
        return recipe;
      })
    );
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${id}`, { withCredentials: true });
  }
}
