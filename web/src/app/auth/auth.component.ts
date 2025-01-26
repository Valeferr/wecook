import { Component } from '@angular/core';

import { AuthFormComponent } from "./auth-form/auth-form.component";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [AuthFormComponent],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {

}
