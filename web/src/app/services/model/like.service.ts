import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private readonly URL: string = 'http://localhost:8080/wecook/post/{postId}/like';

  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post(
    postId: number,
  ): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    const url = this.URL.replace('{postId}', String(postId));
    
    return this.http.post<any>(url, {}, { headers: headers });
  }

  public delete(
    postId: number
  ): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    const url = this.URL.replace('{postId}', String(postId));

    return this.http.delete<any>(url, { headers: headers });
  }
}
