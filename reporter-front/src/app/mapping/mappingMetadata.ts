export class MappingMetadata {
  itemName: string;
  pathToId: string;
  size:number;
  lastUpdated: string;
  fieldsToKeep: any[] = [];
  urlParams: UrlParam[] = [];
  hide: boolean = false;
  editMode: boolean = false;
  running: boolean = false;
}

export class UrlParam {
  key: string;
  value: string;
}
