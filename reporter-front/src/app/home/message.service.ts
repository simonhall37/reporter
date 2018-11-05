import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs';
import {Message} from './message';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

    private emitChangeSource = new Subject<Message>();

    changeEmitted = this.emitChangeSource.asObservable();

    emitChange(message: Message) {
        this.emitChangeSource.next(message);
    }

}
