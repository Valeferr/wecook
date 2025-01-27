import { Component, inject } from '@angular/core';
import { Roles } from '../model/User.model';
import { StandardUser } from '../model/StandardUser.model';
import { ModeratorUser } from '../model/ModeratorUser.model';
import { MainFrameComponent } from "../pages/main-frame/main-frame.component";
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [MainFrameComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  auth: AuthService = inject(AuthService);

  //esempio
  user: StandardUser | ModeratorUser = {
    id: 1,
    email: 'fdgfghg',
    username: 'Nome Cognome',
    password: 'dsf',
    role: Roles.Standard
  };

  Roles = Roles;

  //TODO: implementare la funzione per creare il grafico lato moderatore
  initializeChart(): void {
    const ctx = document.getElementById('reportChart') as HTMLCanvasElement;
  }
}
