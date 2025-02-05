import { Component, inject, input } from '@angular/core';
import { Comment } from '../../../model/Comment.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent {
  public comment = input.required<Comment>();

  protected router: Router = inject(Router);
}
