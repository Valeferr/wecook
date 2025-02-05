import { Component, inject } from '@angular/core';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { Post } from '../../model/Post.model';
import { PostService } from '../../services/model/post.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-saved-posts',
  standalone: true,
  imports: [MainFrameComponent],
  templateUrl: './saved-posts.component.html',
  styleUrl: './saved-posts.component.css'
})
export class SavedPostsComponent {
  protected readonly postService: PostService = inject(PostService);
  protected readonly router: Router = inject(Router);

  posts: Post[] = [];

  constructor() { }

  ngOnInit(): void {
    this.loadSavedPosts();
  }
  //TODO
  private loadSavedPosts(): void {
    // this.postService.getSavedPosts().subscribe({
    //   next: (posts: Post[]) => {
    //     this.posts = posts;
    //   },
    //   error: (err) => {
    //     console.error('Errore nel caricamento dei post salvati:', err);
    //   }
    // });
  }

  onClick(post: Post): void {
    this.router.navigate(['/post', post.id]);
  }
  
}
