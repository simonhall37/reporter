import { Component, OnInit } from '@angular/core';
import {ApiService} from '../home/apiservice.service';
import {MappingMetadata} from '../mapping/mappingMetadata';
import {ReportingMetadata, Inputs,Summary,EnumSummary,FilterDefinition,ColumnDefinition,ReportPair} from './reportingMetadata';
import { HttpErrorResponse } from '@angular/common/http';
import {Message, Type} from '../home/message';
import {MessageService} from '../home/message.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  constructor(private apiService: ApiService, private messageService: MessageService) { }

  reportMetas: ReportingMetadata[];
  apiAddress: string = "reports";
  apiObjNames: string = "apiobjects";
  objectNames: string[] = [];
  filterSummaries: Summary[] = [];
  columnSummaries: Summary[] = [];
  enums: EnumSummary[];
  newReportMeta: ReportingMetadata;

  ngOnInit() {
    this.newReportMeta = new ReportingMetadata();
    this.apiService.getObject(this.apiAddress).subscribe(
      (obj) => {
        this.reportMetas = obj;
      }
    )
    this.apiService.getObject(this.apiObjNames).subscribe(
      (obj: MappingMetadata[]) => {
        for (let meta of obj){
          this.objectNames.push(meta.itemName);
        }
      }
    )
    this.getTypes();
  }

  getTypes(){
    let helper: Inputs = new Inputs();
    this.filterSummaries = helper.filterSummaries;
    this.columnSummaries = helper.columnSummaries;
    this.enums = helper.enums;
  }

  getEnum(name: string): string[]{
    for (let e of this.enums){
      if (e.name === name) return e.values;
    }
    return [];
  }

  hideReportMeta(report: ReportingMetadata){
    report.hide = !report.hide;
  }

  getTabIndex(report: ReportingMetadata): number{
    return this.reportMetas.indexOf(report)+1;
  }

  toggleEditMode(report: ReportingMetadata) {
    report.editMode = !report.editMode;
  }

  toggleFilterEditMode(filter: FilterDefinition,report: ReportingMetadata){
    if (filter.editMode){
      this.updateReport(report);
    }
    filter.editMode = !filter.editMode;
  }

  toggleColumnEditMode(column: ColumnDefinition,report: ReportingMetadata){
    if (column.editMode){
      this.updateReport(report);
    }
    column.editMode = !column.editMode;
  }

  setFilterType(filter: FilterDefinition,newValue: string){
    console.log("Current: " + filter.type);
    console.log("new: " + newValue);
    if (filter.type != newValue){
      filter.type = newValue;
      filter.inputs = [];
      for (let summary of this.filterSummaries){
        if (summary.name === filter.type){
          for (let i in summary.inputNames){
            filter.inputs.push(new ReportPair(summary.inputNames[i],summary.inputTypes[i]));
          }
        }
      }
    }
  }

  setColumnType(column: ColumnDefinition,newValue: string){
    console.log("Current: " + column.type);
    console.log("new: " + newValue);
    if (column.type != newValue){
      column.type = newValue;
      column.inputs = [];
      for (let summary of this.columnSummaries){
        if (summary.name === column.type){
          for (let i in summary.inputNames){
            column.inputs.push(new ReportPair(summary.inputNames[i],summary.inputTypes[i]));
          }
        }
      }
    }
  }

  addFilter(report: ReportingMetadata){
    report.filter.filters.push(new FilterDefinition());
  }

  removeFilter(report: ReportingMetadata, filter: FilterDefinition){
    let index:number = report.filter.filters.indexOf(filter);
    if (index>-1){
      report.filter.filters.splice(index,1);
    }
  }

  hideFilters(report: ReportingMetadata){
    report.filter.hide = !report.filter.hide;
  }

  addColumn(report: ReportingMetadata){
    report.cols.columns.push(new ColumnDefinition());
  }

  removeColumn(report: ReportingMetadata, column: ColumnDefinition){
    let index:number = report.cols.columns.indexOf(column);
    if (index>-1){
      report.cols.columns.splice(index,1);
    }
  }

  hideColumns(report: ReportingMetadata){
    report.cols.hide = !report.cols.hide;
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

  // POST
  addReportMetaKeyPress(event){
    if (event.keyCode === 13){
      this.addReportMeta();
    }
  }

  addReportMeta(){
    if (this.newReportMeta.reportName && this.newReportMeta.reportName.length > 0 && this.newReportMeta.dataSource && this.newReportMeta.reductionType){
      this.apiService.postObject(this.apiAddress,this.newReportMeta).subscribe(
        (obj) => {
          this.reportMessage(Type.SUCCESS,"Report Metadata for " + this.newReportMeta.reportName + " created.");
          this.reportMetas.push(this.newReportMeta);
          this.newReportMeta = new ReportingMetadata();
        },
        (err: HttpErrorResponse) => {
          this.handleError(err);
        }
      );
    }
    else {
      this.reportMessage(Type.WARNING,"Name, data source and reduction type can't be empty");
    }
  }

  // DELETE
  removeReportMeta(report: ReportingMetadata){
    this.apiService.deleteObject(this.apiAddress,report.reportName).subscribe(
      (obj) => {
        let index:number = this.reportMetas.indexOf(report);
        if (index > -1){
          this.reportMetas.splice(index,1);
        }
        this.reportMessage(Type.SUCCESS,"Deleted report metadata for " + report.reportName + ".");
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
  }

  // PUT
  updateReport(report: ReportingMetadata){
    this.apiService.putObject(this.apiAddress,report,report.reportName).subscribe(
      (obj) => {
        this.reportMessage(Type.SUCCESS,"Report Metadata for " + report.reportName + " updated successfully!");
      },
      (err: HttpErrorResponse) => {
        this.handleError(err);
      }
    );
    report.editMode = false;
  }

  // EXECUTE REPORT
  executeQuery(report: ReportingMetadata){
    report.running = true;
    this.apiService.postObject(this.apiAddress + '/' + report.reportName,report).subscribe(
      (obj) => {
        report.running = false;
        console.log(obj);
        this.reportMessage(
          Type.SUCCESS,"Report for " + report.reportName + " executed. " + obj.size + " items returned"
        );

      },
      (err: HttpErrorResponse) => {
        report.running = false;
        console.log(err);
        this.handleError(err);
      }
    );
  }

  downloadFile(report: ReportingMetadata){
    report.running = true;
    this.apiService.postAndReceiveFile(this.apiAddress + '/' + report.reportName + '/csv',report).subscribe(
      (res) => {
        console.log('start download:',res);

          var url = window.URL.createObjectURL(res);
          var a = document.createElement('a');
          document.body.appendChild(a);
          a.setAttribute('style', 'display: none');
          a.href = url;
          a.download = report.reportName + ".csv";
          a.click();
          window.URL.revokeObjectURL(url);
          a.remove();
          this.reportMessage(Type.SUCCESS,"Report downloaded successfully (" + res.size + " bytes)");
          report.running = false;
        }, error => {
          report.running = false;
          console.log('download error:', JSON.stringify(error));
          let failure:Message = new Message(Type.ERROR,error.error[0].message);
          this.messageService.emitChange(failure);
        }, () => {
          console.log('Completed file download.')
        }
    );
  }


}
