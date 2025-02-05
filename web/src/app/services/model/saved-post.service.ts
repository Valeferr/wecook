import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import { SavedPost } from '../../model/SavedPost.model';

@Injectable({
  providedIn: 'root'
})
export class SavedPostService {
  private readonly URL: string = 'http://localhost:8080/wecook/savedPost';

  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post(
    data: {
      postId: number
    }
  ): Observable<SavedPost> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
    
    return this.http.post<SavedPost>(this.URL, data, { headers: headers });
  }

  public delete(
    postId: number
  ): Observable<SavedPost> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<SavedPost>(`${this.URL}/${postId}`, { headers: headers });
  }
}
