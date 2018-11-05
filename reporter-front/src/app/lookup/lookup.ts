import {LookupPair} from './lookupPair';

export class Lookup {
  name: string;
  values: LookupPair[];
  hide: boolean = false;
  editMode: boolean = false;

  public Lookup(){
    this.values = [];
  }
}
