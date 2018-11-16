import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {environment} from '../../environments/environment';
import { Observable } from 'rxjs';
import {User} from './user';

const baseURL: string = environment.BaseUrl;

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(private http: HttpClient) { }

    login(username: string, password: string) {

        let headers = new HttpHeaders({ 'Authorization':'Basic ' +  window.btoa(username + ':' + password)});
        let options = { headers: headers };

        return this.http.post<Observable<boolean>>(baseURL + 'login',{username,password},options)
            .pipe(map(isValid => {

                if (isValid) {
                    let user:User = new User();
                    user.authdata = window.btoa(username + ':' + password);
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }

                return isValid;
            }
          ));

    }

    logout() {
        localStorage.removeItem('currentUser');
    }
}
