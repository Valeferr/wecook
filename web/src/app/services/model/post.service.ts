import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Post } from '../../model/Post.model';
import { map, Observable } from 'rxjs';
import { plainToInstance } from 'class-transformer';
import { Comment } from '../../model/Comment.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
private readonly URL: string = 'http://localhost:8080/wecook/post';

  private http = inject(HttpClient);

  constructor() { }

  public post(
    data: {
      standardUserId: number,
      postPicture: string,
    }
  ): Observable<Post> {
    return this.http.post<Post>(this.URL, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Post, response))
    );
  }

  public get(
    postId: number
  ): Observable<Post> {
    return this.http.get<Post>(`${this.URL}/${postId}`, { withCredentials: true }).pipe(
      map((response) => {
        const post = plainToInstance(Post, response);
        post.comments = post.comments.map(comment => plainToInstance(Comment, comment));
        return post;
      })
    );
  }

  public getAll(): Observable<Array<Post>> {
    return this.http.get<Array<Post>>(this.URL, { withCredentials: true }).pipe(
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
    return this.http.patch<Post>(`${this.URL}/${postId}`, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Post, response))
    );
  }

  public delete(postId: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${postId}`, { withCredentials: true });
  }

  public put(
    postId: number, 
    data: {
      recipeId: number,
    }
  ): Observable<Post> {
    return this.http.put<Post>(`${this.URL}/${postId}`, data, { withCredentials: true }).pipe(
      map((response) => plainToInstance(Post, response))
    );
  }
}
