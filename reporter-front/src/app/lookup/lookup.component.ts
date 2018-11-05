import { Component, OnInit } from '@angular/core';
import {ApiService} from '../home/apiservice.service';
import {Lookup} from './lookup';
import {LookupPair} from './lookupPair';
import { HttpErrorResponse } from '@angular/common/http';
import {Message, Type} from '../home/message';
import {MessageService} from '../home/message.service';

@Component({
  selector: 'app-lookup',
  templateUrl: './lookup.component.html',
  styleUrls: ['./lookup.component.scss']
})
export class LookupComponent implements OnInit {

  constructor(private apiService: ApiService, private messageService: MessageService) { }

  lookups: Lookup[];
  newLookupName: string;
  apiAddress: string = "lookups";

  ngOnInit() {
    this.apiService.getObject(this.apiAddress).subscribe(
      (obj) => {
        this.lookups=obj;
      }
    )
  }

  hideLookup(lookup: Lookup){
    lookup.hide = !lookup.hide;
  }

  getTabIndex(lookup: Lookup): number{
    return this.lookups.indexOf(lookup)+1;
  }

  toggleEditMode(lookup: Lookup) {
    lookup.editMode = !lookup.editMode;
  }

  addPair(lookup:Lookup){
    lookup.values.unshift(new LookupPair());
    lookup.editMode = true;
  }

  removePair(lookup:Lookup,pair:LookupPair){
    let index:number = lookup.values.indexOf(pair);
    if (index>-1) {
      lookup.values.splice(index,1);
      this.updateLookup(lookup);
      lookup.editMode=false;
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

  updateLookupKeyPress(event,lookup:Lookup){
    if (event.keyCode === 13){
      this.updateLookup(lookup);
    }
  }

  updateLookup(lookup: Lookup){
    this.apiService.putObject(this.apiAddress,lookup,lookup.name).subscribe(
      (obj) => {
        this.reportMessage(Type.SUCCESS,"Lookup " + lookup.name + " updated successfully!");
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
    this.toggleEditMode(lookup);
  }

  addLookupKeyPress(event){
    if (event.keyCode === 13){
      this.addLookup();
    }
  }

  addLookup(){
    if (this.newLookupName && this.newLookupName.length>0){
      let toAdd:Lookup = new Lookup();
      toAdd.name = this.newLookupName;
      toAdd.values = [];
      this.apiService.postObject(this.apiAddress,toAdd).subscribe(
        (obj) => {
          this.reportMessage(Type.SUCCESS,this.newLookupName + " created!");
          this.lookups.push(toAdd);
          this.newLookupName = null;
        },
        (err: HttpErrorResponse) => {
          this.handleError(err);
        }
      );
    }
    else {
      this.reportMessage(Type.WARNING,"Lookup name can't be null");
    }
  }

  removeLookup(lookup:Lookup){
    this.apiService.deleteObject(this.apiAddress,lookup.name).subscribe(
      (obj) => {
        let index:number = this.lookups.indexOf(lookup);
        if (index > -1){
          this.lookups.splice(index,1);
        }
        this.reportMessage(Type.SUCCESS,lookup.name + " deleted successfully!");
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
  }

}
