import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {environment} from '../../environments/environment';
import { Observable } from 'rxjs';
import {User} from './user';

const baseURL: string = environment.BaseUrl;

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(private http: HttpClient) { }

    login(username: string, password: string) {

        return this.http.post<Observable<boolean>>(baseURL + 'login',{username,password})
            .pipe(map(isValid => {
                if (isValid) {
                    this.update(username,password);
                }
                return isValid;
            }
          ));
    }

    update(username: string, password: string){
      let user:User = new User();
      user.authdata = window.btoa(username + ':' + password);
      localStorage.setItem('currentUser', JSON.stringify(user));
    }

    wipeToken(){
      localStorage.removeItem('currentUser');
    }

    logout() {
        return this.http.post<Observable<boolean>>(baseURL + 'customLogout',null)
          .pipe(map(
            (obj) => {
              this.wipeToken();
              return true;
            } ,
            ( err: HttpErrorResponse) => {
              console.error("Error!");
              this.wipeToken();
              console.log(err.status + " --- " + err.message);
            }
        ));
    }
}
