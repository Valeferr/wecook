import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { AuthComponent } from './auth/auth.component';

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
  // {
  //   path: '**',
  //   component: PageNotFoundComponent
  // }
];
