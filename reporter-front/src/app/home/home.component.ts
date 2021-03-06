import { Component, OnInit } from '@angular/core';
import {Message,Type} from './message';
import {MessageService} from './message.service';
import {AuthService} from '../security/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(
    private messageService: MessageService,
    private router: Router,
    private authService: AuthService
  ) {
    this.message = new Message(Type.SUCCESS,"");
    messageService.changeEmitted.subscribe(
        (message:Message) => {
            message.show = true;
            this.message = message;
        });
  }

  message: Message;

  ngOnInit() {

  }

  toggleMessageVis(){
    this.message.show = !this.message.show;
  }

  isError():boolean{
    return this.message.type == Type.ERROR;
  }

  isWarning():boolean{
    return this.message.type == Type.WARNING;
  }

  isSuccess():boolean{
    return this.message.type == Type.SUCCESS;
  }

  logout(){
    this.authService.logout().subscribe(
      (obj) => {
        this.router.navigate(['/login']);
      }
    );
  }
}
