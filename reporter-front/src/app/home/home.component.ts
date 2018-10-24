import { Component, OnInit } from '@angular/core';
import {ApiService} from './apiservice.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private apiService: ApiService) { }

  message: string;

  ngOnInit() {
    this.apiService.getMessage().subscribe(
      (message) => {
        console.log(message);
        this.message = message.message;
      }
    )
  }

}
