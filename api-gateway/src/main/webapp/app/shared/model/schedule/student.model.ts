import { IScheduledclass } from 'app/shared/model/schedule/scheduledclass.model';

export interface IStudent {
    id?: number;
    name?: string;
    email?: string;
    scheduledClasses?: IScheduledclass[];
}

export class Student implements IStudent {
    constructor(public id?: number, public name?: string, public email?: string, public scheduledClasses?: IScheduledclass[]) {}
}
