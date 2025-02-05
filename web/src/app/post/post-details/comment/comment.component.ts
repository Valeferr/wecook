import { Component, input } from '@angular/core';
import { Comment } from '../../../model/Comment.model';

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent {
  public comment = input.required<Comment>();
}
