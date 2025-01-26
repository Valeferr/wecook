import { Component, inject, output } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Roles, User } from '../../model/User.model';
import { AuthService } from '../../services/auth.service';
import { StandardUser } from '../../model/StandardUser.model';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {
  public invalidCredentials = output<string>();
  public success = output<void>();

  private auth = inject(AuthService);
  private formBulder = inject(FormBuilder);
  private router = inject(Router);

  protected registerForm: FormGroup
  public email: string = '';
  public username: string = '';
  public password: string = '';
  public passwordConfirmation: string = '';

  constructor() {
    this.registerForm = this.formBulder.group({
      username: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.matchCostraints()]],
      passwordConfirmation: ['', [Validators.required] ]
    });
  }

  private matchCostraints(): (control: AbstractControl) => { matchCostraints: boolean } | null {
    return (control: AbstractControl) => {
      const value = control.value || '';
      const hasSpecialChar = /[!#$%^&*,.?]/.test(value);
      const hasNumber = /[0-9]/.test(value);
      const hasUpperCase = /[A-Z]/.test(value);
      const hasLowerCase = /[a-z]/.test(value);
      return (
        hasSpecialChar &&
        hasNumber &&
        hasUpperCase &&
        hasLowerCase
      ) ? null : { matchCostraints: true };
    };
  }

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

  public onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }
    
    this.auth.register({
      email: this.email,
      username: this.username,
      password: this.password,
      role: Roles.Standard
    }).subscribe({
      next: (user: StandardUser) => {
        this.success.emit();
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 409) {
          this.invalidCredentials.emit('Email or username already in use');
        } else {
          this.router.navigate(['/error'], { state: { statusCode: error.status } });
        }
      }
    });
  }
}
