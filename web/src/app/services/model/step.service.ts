import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Step } from '../../model/Step.model';
import { plainToInstance } from 'class-transformer';
import { RecipeIngredient } from '../../model/RecipeIngredient.model';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class StepService {
  private readonly URL: string = 'http://localhost:8080/wecook/recipe/{recipeId}/step';
  
  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post(
    recipeId: number,
    data: {
      description: string,
      duration: number,
      action: string,
      stepIndex: number
    }
  ): Observable<Step> {
    const url = this.URL.replace("{recipeId}", String(recipeId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<Step>(url, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Step, response))
    );
  }

  public get(
    recipeId: number,
    stepId: number
  ): Observable<Step> {
    const url = this.URL.replace("{recipeId}", String(recipeId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Step>(`${url}/${stepId}`, { headers: headers }).pipe(
      map((response) => {
        const step = plainToInstance(Step, response);
        step.ingredients = step.ingredients.map((i) => plainToInstance(RecipeIngredient, i));
        return step;
      })
    );
  }

  public getAll(
    recipeId: number
  ): Observable<Array<Step>> {
    const url = this.URL.replace("{recipeId}", String(recipeId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<Step>>(url, { headers: headers }).pipe(
      map((response) => response.map((s) => {
        const step = plainToInstance(Step, s);
        step.ingredients = step.ingredients.map((i) => plainToInstance(RecipeIngredient, i));
        return step;
      })
    ));
  }

  public patch(
    recipeId: number,
    stepId: number,
    data: Partial<{
      description: string,
      duration: number,
      action: string,
      stepIndex: number
    }>
  ): Observable<Step> {
    const url = this.URL.replace("{recipeId}", String(recipeId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<Step>(`${url}/${stepId}`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Step, response))
    );
  }

  public delete(
    recipeId: number,
    stepId: number
  ): Observable<void> {
    const url = this.URL.replace("{recipeId}", String(recipeId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<void>(`${url}/${stepId}`, { headers: headers });
  }
}
