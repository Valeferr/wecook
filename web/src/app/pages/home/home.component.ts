import { Component, inject, OnInit } from '@angular/core';
import { MainFrameComponent } from '../main-frame/main-frame.component';
import { PostComponent } from '../../post/post.component';
import { PostService } from '../../services/model/post.service';
import { firstValueFrom } from 'rxjs';
import { Post } from '../../model/Post.model';
import { AuthService } from '../../services/auth.service';
import { Roles } from '../../model/User.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MainFrameComponent, PostComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  private readonly postService = inject(PostService);
  protected readonly authService = inject(AuthService);

  Roles = Roles;
  public posts: Array<Post> = new Array<Post>();
  
  constructor() {}

  async ngOnInit() {
    this.posts = await firstValueFrom(this.postService.getAll());
  }
}