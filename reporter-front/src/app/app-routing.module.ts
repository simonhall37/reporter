import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import {LookupComponent} from './lookup/lookup.component';
import {AuthGuard} from './security/auth.guard';

const routes: Routes = [
  {
    path: '', component: HomeComponent ,canActivate: [AuthGuard],
    children: [
      { path: 'lookups', component: LookupComponent ,canActivate: [AuthGuard] }
    ]
    },
   {
     path: 'login', component: LoginComponent
   },
   {
     path: '**', redirectTo: ''
   }
];

export const routing = RouterModule.forRoot(routes);
