import { Component, OnInit } from '@angular/core';
import {ApiService} from '../home/apiservice.service';
import { HttpErrorResponse } from '@angular/common/http';
import {Message, Type} from '../home/message';
import {MessageService} from '../home/message.service';
import {AuthService} from '../security/auth.service';
import {User} from './user';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  constructor(
    private apiService: ApiService,
    private messageService: MessageService
    private authService: AuthService
  ) { }

  apiAddress: string = "user";
  user: User;
  passResetShow: boolean = false;
  pass1: string = "";
  pass2: string = "";
  MIN_PASS_LEN: number = 6;

  ngOnInit() {
    this.user = new User();
    this.apiService.getObject(this.apiAddress).subscribe(
      (obj) => {
        this.user = obj;
      }
    )
  }

  toggleEditMode(){
    this.user.editMode = !this.user.editMode;
  }

  toggleResetPassword(){
    this.passResetShow = !this.passResetShow;
  }

  verifyPassLength(pass: string): boolean {
    if (pass.length < this.MIN_PASS_LEN){
        console.log("pass too short");
        return false;
    }
    else return true;
  }

  verifyPassMatch(): boolean{
    if (this.pass1 === this.pass2)
      return true;
    else {
      console.log("pass don't match");
      return false;
    }
  }

  updatePassword(){
    if (!this.verifyPassLength(this.pass1) || !this.verifyPassLength(this.pass2)) {
      this.messageService.emitChange(new Message(Type.WARNING,"The new password length must be at least " + this.MIN_PASS_LEN + " characters"));
      return;
    }
    if (!this.verifyPassMatch()) {
      this.messageService.emitChange(new Message(Type.WARNING,"The new passwords don't match!"));
      return;
    }
    this.user.password = this.pass1;
    this.updateUser();
  }

  updatePasswordKeyPress(event){
    if (event.keyCode === 13){
      this.updatePassword();
    }
  }

  updateUserKeyPress(event){
    if (event.keyCode === 13){
      this.updateUser();
    }
  }

  reportMessage(type: Type,message: string){
    let toEmit:Message = new Message(type,message);
    this.messageService.emitChange(toEmit);
  }

  handleError(err: HttpErrorResponse){
    let failure:Message = new Message(Type.ERROR,err.error[0].message);
    this.messageService.emitChange(failure);
    console.log(err.status + ": " + err.error[0].message);
  }

  updateUser(){
    this.apiService.putObject(this.apiAddress,this.user,"").subscribe(
      (obj) => {
        this.reportMessage(Type.SUCCESS,"User updated successfully!");
        this.authService.update(this.user.username,this.user.password);
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
    this.user.editMode = false;
    this.passResetShow = false;
  }

}
