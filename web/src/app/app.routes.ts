import { Routes } from '@angular/router';

import { authGuard } from './guards/auth.guard';
import { AuthComponent } from './auth/auth.component';
import { ErrorComponent } from './error/error.component';
import { HomeComponent } from './pages/home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { PostPublishComponent } from './post-publish/post-publish.component';

export const routes: Routes = [
  {
    path: 'login',
    component: AuthComponent,
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard],
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [authGuard],
  },
  {
    path: 'post-publish',
    component: PostPublishComponent,
    canActivate: [authGuard],
  },
  {
    path: 'error',
    component: ErrorComponent,
  },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: '**',
    component: ErrorComponent,
    data: { statusCode: '404' }
  }
];
