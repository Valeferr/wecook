import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommentComponent } from "./comment/comment.component";

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [FormsModule, CommentComponent],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.css'
})
export class PostDetailsComponent {
  public comment: string = '';
}
