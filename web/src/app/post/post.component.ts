import { Component, inject, input } from '@angular/core';
import { PostDetailsComponent } from "./post-details/post-details.component";
import { Post } from '../model/Post.model';
import { LikeService } from '../services/model/like.service';
import { SavedPostService } from '../services/model/saved-post.service';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [PostDetailsComponent],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent {
  public post = input.required<Post>();

  private readonly likeService = inject(LikeService);
  private readonly savedPostService = inject(SavedPostService);

  public onLike() {
    this.likeService.post(this.post().id).subscribe(() => {
      this.post().liked = true;
      this.post().likes++;
    });
  }

  public onUnlinke() {
    this.likeService.delete(this.post().id).subscribe(() => {
      this.post().liked = false;
      this.post().likes--;
    });
  }

  public onSave() {
    this.savedPostService.post({
      postId: this.post().id
    }).subscribe(() => {
      this.post().saved = true;
    })
  }

  public onUnsave() {
    this.savedPostService.delete(this.post().id).subscribe(() => {
      this.post().saved = false;
    })
  }
}
