import { Component } from '@angular/core';
import { MainFrameComponent } from "../main-frame/main-frame.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MainFrameComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
