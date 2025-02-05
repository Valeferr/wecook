import { Component, inject, output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../model/User.model';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {
  private auth = inject(AuthService);
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  protected loginForm: FormGroup;
  public email: string = '';
  public password: string = '';

  invalidCredentials = output<string>();

  constructor() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  public togglePassword() {
    const passwordElement = document.getElementById('password')!;

    const type = passwordElement.getAttribute('type');
    if (type === 'password') {
      passwordElement.setAttribute('type', 'text');
    } else {
      passwordElement.setAttribute('type', 'password');
    }
  }

  public onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    
    this.auth.login({
      email: this.email,
      password: this.password,
    }).subscribe({
      next: (user: User) => {
        const navigateTo = this.route.snapshot.queryParams['returnUrl'] || '/home';
        this.router.navigate([navigateTo]);  
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.invalidCredentials.emit('Invalid username or password');
        } else {
          this.router.navigate(['/error'], { queryParams: { statusCode: error.status } });
        }
      }
    });
  }
}
