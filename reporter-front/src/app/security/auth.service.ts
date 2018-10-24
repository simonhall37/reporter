import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {environment} from '../../environments/environment';
import { Observable } from 'rxjs';
import {User} from './user';

const APIurl: string = environment.API;

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(private http: HttpClient) { }

    login(userName: string, password: string) {
        return this.http.post<Observable<boolean>>(APIurl + 'login', { userName, password })
            .pipe(map(isValid => {
                // login successful if there's a user in the response
                if (isValid) {
                    // store user details and basic auth credentials in local storage
                    // to keep user logged in between page refreshes
                    let user:User = new User();
                    user.authdata = window.btoa(userName + ':' + password);
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }

                return isValid;
            }));

    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }
}
