import { Component, inject, input, output } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommentComponent } from "./comment/comment.component";
import { Post } from '../../model/Post.model';
import { CommentService } from '../../services/model/comment.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [FormsModule, CommentComponent, ReactiveFormsModule],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.css'
})
export class PostDetailsComponent {
  public post = input.required<Post>();

  private readonly commentService = inject(CommentService);
  private readonly authSevice = inject(AuthService);
  protected readonly router = inject(Router);

  protected commentForm: FormGroup = new FormGroup({
    text: new FormControl<string | null>(null, [
      Validators.minLength(1),
      Validators.maxLength(400)
    ])
  });

  constructor() {}

  onSubmitComment() {
    this.commentService.post(this.post().id,{
        text: this.commentForm.value.text
      }
    ).subscribe((comment) => {
      this.post().comments.push(comment);
      this.commentForm.reset();
    });
  }
}
