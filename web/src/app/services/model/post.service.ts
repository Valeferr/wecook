import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Post } from '../../model/Post.model';
import { map, Observable } from 'rxjs';
import { plainToInstance } from 'class-transformer';
import { Comment } from '../../model/Comment.model';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
private readonly URL: string = 'http://localhost:8080/wecook/post';

  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  constructor() {}

  public post(
    data: {
      postPicture: string,
    }
  ): Observable<Post> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<Post>(this.URL, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Post, response))
    );
  }

  public searchByCategory (
    category: string
  ): Observable<Array<Post>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<Array<Post>>(`${this.URL}/searchByName`, category ).pipe(
          map((response) => response.map((p) => plainToInstance(Post, p)))
        );
  }

  public get(
    postId: number
  ): Observable<Post> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Post>(`${this.URL}/${postId}`, { headers: headers }).pipe(
      map((response) => {
        const post = plainToInstance(Post, response);
        post.comments = post.comments.map(comment => plainToInstance(Comment, comment));
        return post;
      })
    );
  }

  public getAll(): Observable<Array<Post>> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.get<Array<Post>>(this.URL, { headers: headers }).pipe(
      map((response) => response.map((p) => {
        const post = plainToInstance(Post, p);
        post.comments = p.comments.map(comment => plainToInstance(Comment, comment));
        return post;
      }))
    );
  }

  public patch(
    postId: number,
    data: Partial<{
      postPicture: string,
    }>
  ): Observable<Post> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.patch<Post>(`${this.URL}/${postId}`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Post, response))
    );
  }

  public delete(postId: number): Observable<void> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<void>(`${this.URL}/${postId}`, { headers: headers });
  }

  public put(
    postId: number, 
    data: {
      recipeId: number,
    }
  ): Observable<Post> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.put<Post>(`${this.URL}/${postId}`, data, { headers: headers }).pipe(
      map((response) => plainToInstance(Post, response))
    );
  }
}
