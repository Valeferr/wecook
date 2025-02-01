import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Roles } from '../../model/User.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-frame',
  standalone: true,
  imports: [],
  templateUrl: './main-frame.component.html',
  styleUrl: './main-frame.component.css'
})
export class MainFrameComponent {
  auth: AuthService = inject(AuthService);
  router: Router = inject(Router);
  
  Roles = Roles;

  public navigateTo(path: string) {
    this.router.navigate([path]);
  }
}
