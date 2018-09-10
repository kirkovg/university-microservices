import { Moment } from 'moment';

export interface IExam {
    id?: number;
    name?: string;
    location?: string;
    time?: Moment;
}

export class Exam implements IExam {
    constructor(public id?: number, public name?: string, public location?: string, public time?: Moment) {}
}
