import { Component, input } from '@angular/core';
import { PostDetailsComponent } from "./post-details/post-details.component";
import { Post } from '../model/Post.model';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [PostDetailsComponent],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent {
  public post = input.required<Post>();

  public onToggleSummary(event: Event): void {
    const detailsElement = (event.target as HTMLDetailsElement);
    const summaryElement = detailsElement.querySelector('summary');

    if (detailsElement.open && summaryElement) {
      summaryElement.textContent = '';
    } else if (summaryElement) {
      summaryElement.textContent = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s..."
    }
  }
}
