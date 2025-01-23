import { Component } from '@angular/core';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {
  public togglePassword() {
    const passwordElement = document.getElementById('password')!;

    const type = passwordElement.getAttribute('type');
    if (type === 'password') {
      passwordElement.setAttribute('type', 'text');
    } else {
      passwordElement.setAttribute('type', 'password');
    }
  }
}
