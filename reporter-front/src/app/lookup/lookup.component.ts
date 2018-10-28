import { Component, OnInit } from '@angular/core';
import {ApiService} from './apiservice.service';

@Component({
  selector: 'app-lookup',
  templateUrl: './lookup.component.html',
  styleUrls: ['./lookup.component.scss']
})
export class LookupComponent implements OnInit {

  constructor(private apiService: ApiService) { }

  model: any;

  ngOnInit() {

    this.apiService.getObject("message").subscribe(
      (obj) => {
        this.model=obj;
      }
    )
  }

}
