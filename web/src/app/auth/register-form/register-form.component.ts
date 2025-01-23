import { Component } from '@angular/core';

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {
  public togglePassword() {
    const passwordElement = document.getElementById('password')!;
    const passwordConfirmationElement = document.getElementById('password-confirmation')!;

    const type = passwordElement.getAttribute('type');
    if (type === 'password') {
      passwordElement.setAttribute('type', 'text');
      passwordConfirmationElement.setAttribute('type', 'text');
    } else {
      passwordElement.setAttribute('type', 'password');
      passwordConfirmationElement.setAttribute('type', 'password');
    }
  }
}
