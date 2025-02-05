import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { plainToInstance } from 'class-transformer';
import { map, Observable } from 'rxjs';
import { Comment } from '../../model/Comment.model';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
private readonly URL: string = 'http://localhost:8080/wecook/post/{postId}/comment';

  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post(
    postId: number,
    data: { text: string }
  ): Observable<Comment> {
    const url = this.URL.replace("{postId}", String(postId));
  
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
  
    return this.http.post<Comment>(url, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Comment, response))
    );
  }

  public get(
    postId: number,
    commentId: number
  ): Observable<Comment> {
    const url = this.URL.replace("{postId}", String(postId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Comment>(`${url}/${commentId}`, { headers: headers }).pipe(
      map((response) => plainToInstance(Comment, response))
    );
  }

  public getAll(): Observable<Array<Comment>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<Comment>>(this.URL, { headers: headers }).pipe(
      map((response) => response.map((comment) => plainToInstance(Comment, comment)))
    );
  }

  public patch(
    postId: number,
    commentId: number,
    data: Partial<{
      status: string
    }>
  ): Observable<Comment> {
    const url = this.URL.replace("{postId}", String(postId));
    
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<Comment>(`${url}/${commentId}`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Comment, response))
    );
  }

  public delete(
    postId: number,
    commentId: number
  ): Observable<void> {
    const url = this.URL.replace("{postId}", String(postId));

    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<void>(`${url}/${commentId}`, { headers: headers });
  }
}
