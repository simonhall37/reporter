import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from '../security/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    model: any = {};
    badCred: boolean = false;
    returnUrl: string;
    serverErr: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private http: HttpClient,
        private authService: AuthService
    ) { }

    ngOnInit() {
      this.authService.logout();
      this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    login() {
      this.badCred = false;
      this.serverErr = false;
        this.authService.login(this.model.username,this.model.password).subscribe(isValid => {
            if (isValid) {
                this.router.navigate([this.returnUrl]);
                this.badCred = false;
            } else {
                this.badCred = true;
            }
        },
        (err: HttpErrorResponse) => {
          if (err.status === 401){
            this.badCred = true;
          }
          else {
            this.serverErr = true;
          }
        }
      );
    }
}
