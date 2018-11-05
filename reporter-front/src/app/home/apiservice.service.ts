import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

const API = environment.API;

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  public getObject(adr: string):Observable<any>{
    return this.httpClient.get<any>(API+ adr);
  }

  public putObject(adr: string,item: any,itemIdentifier: string){
    return this.httpClient.put<any>(API+adr+'/'+itemIdentifier,item);
  }

  public deleteObject(adr: string,itemIdentifier: string){
    return this.httpClient.delete<any>(API+adr+'/'+itemIdentifier);
  }

  public postObject(adr: string,item: any){
    return this.httpClient.post<any>(API + adr,item);
  }

}
