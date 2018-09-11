import { Moment } from 'moment';

export interface ILine {
    id?: number;
    name?: string;
    universityStopTime?: Moment;
}

export class Line implements ILine {
    constructor(public id?: number, public name?: string, public universityStopTime?: Moment) {}
}
