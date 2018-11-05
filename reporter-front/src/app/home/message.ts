export class Message {
  type: Type;
  message: string;
  show: boolean = false;

  constructor(type:Type,message: string){
    this.type = type;
    this.message = message;
  }
}

export enum Type {
  ERROR = "ERROR",
  WARNING = "WARNING",
  SUCCESS = "SUCCESS"
}
