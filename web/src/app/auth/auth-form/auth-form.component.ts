import { Component, Input } from '@angular/core';
import { LoginFormComponent } from "../login-form/login-form.component";
import { RegisterFormComponent } from "../register-form/register-form.component";

@Component({
  selector: 'app-auth-form',
  standalone: true,
  imports: [LoginFormComponent, RegisterFormComponent],
  templateUrl: './auth-form.component.html',
  styleUrl: './auth-form.component.css'
})
export class AuthFormComponent {
  @Input({required: true}) formType!: 'login' | 'register';

  protected authError: string = '';

  setFormType(formType: 'login' | 'register') {
    this.authError = '';
    this.formType = formType;
  }

  showErrorMessage(message: string) {
    this.authError = message;
  }
}
