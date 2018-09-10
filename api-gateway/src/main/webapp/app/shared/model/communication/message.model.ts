import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/communication/appuser.model';

export interface IMessage {
    id?: number;
    content?: string;
    sentAt?: Moment;
    from?: IAppuser;
    to?: IAppuser;
}

export class Message implements IMessage {
    constructor(public id?: number, public content?: string, public sentAt?: Moment, public from?: IAppuser, public to?: IAppuser) {}
}
