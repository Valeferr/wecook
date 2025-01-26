import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Roles } from '../../model/User.model';

@Component({
  selector: 'app-main-frame',
  standalone: true,
  imports: [],
  templateUrl: './main-frame.component.html',
  styleUrl: './main-frame.component.css'
})
export class MainFrameComponent {
  auth: AuthService = inject(AuthService);
  
  Roles = Roles;
}
