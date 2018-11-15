export class ReportingMetadata {
  reportName: string;
  dataSource: string;
  reductionType: ReduceOps;
  cols: ColumnMetadata;
  filter: FilterMetadata;
  hide: boolean = false;
  editMode: boolean = false;
  running: boolean = false;

  constructor(){
    this.cols = new ColumnMetadata();
    this.filter = new FilterMetadata();
  }
}

export class ColumnMetadata {
  columns: ColumnDefinition[] = [];
  hide: boolean = false;
}

export class FilterMetadata {
  filters: FilterDefinition[] = [];
  hide: boolean = false;
}

export class ColumnDefinition {
  type:string;
  columnType: string;
  colName: string;
  key: boolean;
  outputType: ColType;
  colNum: number;
  inputs: ReportPair[] = [];
  editMode: boolean = false;
}

export class FilterDefinition {
  type:string;
  inputs: ReportPair[] = [];
  editMode: boolean = false;
}

export class ReportPair {
  key: string;
  value: string;
  type: string;

  constructor(key: string, type: string){
    this.key = key;
    this.type = type;
  }
}

export class Summary{
  name: string;
  inputNames: string[];
  inputTypes: string[];

  constructor(name:string,inputNames:string[],inputTypes:string[]){
    this.inputNames = inputNames;
    this.inputTypes = inputTypes;
    this.name = name;
  }
}

export class Inputs {
  filterSummaries: Summary[] = [];
  columnSummaries: Summary[] = [];
  enums: EnumSummary[] = [];

  constructor(){
    this.filterSummaries.push(new Summary("numeric",["fieldToCheck","lowerValue","upperValue","comp"],["string","number","number","NumComp"]));
    this.filterSummaries.push(new Summary("contains",["fieldToCheck","ignoreCase","shouldContain"],["string","boolean","string"]));

    this.columnSummaries.push(new Summary("combine",["field","delim"],["string","string"]));
    this.columnSummaries.push(new Summary("lookup",["field","lookup","default"],["string","string","string"]));
    this.columnSummaries.push(new Summary("simple",["field"],["string"]));
    this.columnSummaries.push(new Summary("weeknum",["field"],["string"]));

    this.enums.push(new EnumSummary("ColType",Object.keys(ColType)));
    this.enums.push(new EnumSummary("NumComp",Object.keys(NumComp)));
    this.enums.push(new EnumSummary("ReduceOps",Object.keys(ReduceOps)));

  }

}

export class EnumSummary{
  name: string;
  values: string[];

  constructor(name: string,values: string[]){
    this.name = name;
    this.values = values;
  }
}

export enum NumComp {
  GREATER_THAN="GREATER_THAN",
	LESS_THAN="LESS_THAN",
	EQUAL="EQUAL",
	BETWEEN="BETWEEN"
}

export enum ColType {
  STRING = "STRING",
	INTEGER = "INTEGER",
	DOUBLE = "DOUBLE"
}

export enum ReduceOps {
  SUM = "SUM",
  COUNT = "COUNT"
}
