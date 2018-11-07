import { Component, OnInit } from '@angular/core';
import {ApiService} from '../home/apiservice.service';
import {MappingMetadata, UrlParam} from './mappingMetadata';
import { HttpErrorResponse } from '@angular/common/http';
import {Message, Type} from '../home/message';
import {MessageService} from '../home/message.service';

@Component({
  selector: 'app-mapping',
  templateUrl: './mapping.component.html',
  styleUrls: ['./mapping.component.scss']
})
export class MappingComponent implements OnInit {

  constructor(private apiService: ApiService, private messageService: MessageService) { }

  mappings: MappingMetadata[];
  apiAddress: string = "apiobjects";
  newMapping: MappingMetadata;

  ngOnInit() {
    this.newMapping = new MappingMetadata();
    this.apiService.getObject(this.apiAddress).subscribe(
      (obj) => {
        this.mappings = obj;
      }
    )
  }

  trackByIdx(index: number, obj: any): any {
    return index;
  }

  hideMapping(mapping: MappingMetadata){
    mapping.hide = !mapping.hide;
  }

  getTabIndex(mapping: MappingMetadata): number{
    return this.mappings.indexOf(mapping)+1;
  }

  toggleEditMode(mapping: MappingMetadata) {
    mapping.editMode = !mapping.editMode;
  }

  removeField(mapping: MappingMetadata, key: string){
    let index:number = mapping.fieldsToKeep.indexOf(key);
    console.log("Must remove " + key);
    if (index > -1){
      mapping.fieldsToKeep.splice(index,1);
      this.updateMapping(mapping);
    }
    else {
      console.log("Couldn't find key " + key);
    }
  }

  removeParam(mapping: MappingMetadata, param: UrlParam){
    let index:number = mapping.urlParams.indexOf(param);
    console.log("Must remove " + param);
    if (index > -1){
      mapping.urlParams.splice(index,1);
      this.updateMapping(mapping);
    }
    else {
      console.log("Couldn't find param " + param.key + ", " + param.value);
    }
  }

  updateMappingKeyPress(event,mapping:MappingMetadata,key:string){
    if (event.keyCode === 13){
      this.updateMapping(mapping);
    }
  }

  updateMapping(mapping: MappingMetadata){
    this.apiService.putObject(this.apiAddress,mapping,mapping.itemName).subscribe(
      (obj) => {
        this.reportMessage(Type.SUCCESS,"Mapping for " + mapping.itemName + " object updated successfully!");
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
    mapping.editMode = false;
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

  addField(mapping: MappingMetadata){
    mapping.fieldsToKeep.push("");
    mapping.editMode = true;
  }

  addUrlParam (mapping: MappingMetadata){
    mapping.urlParams.push(new UrlParam());
    mapping.editMode= true;
  }

  executeQuery(mapping: MappingMetadata){
    mapping.running = true;
    this.apiService.postObject(this.apiAddress + '/' + mapping.itemName,mapping).subscribe(
      (obj) => {
        this.reportMessage(
          Type.SUCCESS,"Mapping for " + mapping.itemName + " executed. " + obj.size + " items returned"
        );
        mapping.running = false;
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
  }

  addMappingKeyPress(event){
    if (event.keyCode === 13){
      console.log("here2");
      this.addMapping();
    }
  }

  removeMapping(mapping: MappingMetadata){
    this.apiService.deleteObject(this.apiAddress,mapping.itemName).subscribe(
      (obj) => {
        let index:number = this.mappings.indexOf(mapping);
        if (index > -1){
          this.mappings.splice(index,1);
        }
        this.reportMessage(Type.SUCCESS,"Deleted mapping for " + mapping.itemName + ".");
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
  }

  addMapping(){
    if (this.newMapping.itemName && this.newMapping.itemName.length > 0 && this.newMapping.pathToId && this.newMapping.pathToId.length > 0){
      this.apiService.postObject(this.apiAddress,this.newMapping).subscribe(
        (obj) => {
          this.reportMessage(Type.SUCCESS,"Mapping for " + this.newMapping.itemName + " created.");
          this.mappings.push(this.newMapping);
          this.newMapping = new MappingMetadata();
        },
        (err: HttpErrorResponse) => {
          this.handleError(err);
        }
      );
    }
    else {
      this.reportMessage(Type.WARNING,"Name and path to id can't be empty");
    }
  }

}
