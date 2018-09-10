import { Moment } from 'moment';

export interface ITask {
    id?: number;
    courseName?: string;
    description?: string;
    dueDate?: Moment;
}

export class Task implements ITask {
    constructor(public id?: number, public courseName?: string, public description?: string, public dueDate?: Moment) {}
}
