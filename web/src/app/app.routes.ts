import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { AuthComponent } from './auth/auth.component';
import { ErrorComponent } from './error/error.component';

export const routes: Routes = [
  {
    path: 'login',
    component: AuthComponent,
  },
  // {
  //   path: 'home',
  //   component: HomeComponent,
  //   canActivate: [authGuard],
  // },
  // {
  //   path: '',
  //   redirectTo: '/home',
  //   pathMatch: 'full'
  // },
  {
    path: '**',
    component: ErrorComponent,
    data: { statusCode: '404' }
  }
];
