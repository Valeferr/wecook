import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import { SavedPost } from '../../model/SavedPost.model';
import { Post } from '../../model/Post.model';
import { plainToInstance } from 'class-transformer';
import { Comment } from '../../model/Comment.model';

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

  public delete(
    postId: number
  ): Observable<SavedPost> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });

    return this.http.delete<SavedPost>(`${this.URL}/${postId}`, { headers: headers });
  }
}
