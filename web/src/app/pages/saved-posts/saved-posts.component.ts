import { Component, inject, OnInit } from '@angular/core';
import { MainFrameComponent } from "../main-frame/main-frame.component";
import { Post } from '../../model/Post.model';
import { Router } from '@angular/router';
import { SavedPostService } from '../../services/model/saved-post.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-saved-posts',
  standalone: true,
  imports: [MainFrameComponent],
  templateUrl: './saved-posts.component.html',
  styleUrl: './saved-posts.component.css'
})
export class SavedPostsComponent implements OnInit {
  protected readonly savedPostService = inject(SavedPostService);
  protected readonly router: Router = inject(Router);

  protected savedPosts: Array<Post> = new Array<Post>();

  constructor() { }

  async ngOnInit() {
    this.savedPosts = await firstValueFrom(this.savedPostService.getAll());
  }
}