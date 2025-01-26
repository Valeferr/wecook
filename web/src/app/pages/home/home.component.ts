import { Component } from '@angular/core';
import { MainFrameComponent } from '../main-frame/main-frame.component';
import { PostComponent } from '../../post/post.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MainFrameComponent, PostComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
 
}
