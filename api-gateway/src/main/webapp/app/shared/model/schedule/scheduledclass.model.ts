import { Moment } from 'moment';

export interface IScheduledclass {
    id?: number;
    courseName?: string;
    description?: string;
    lecturer?: string;
    location?: string;
    date?: Moment;
}

export class Scheduledclass implements IScheduledclass {
    constructor(
        public id?: number,
        public courseName?: string,
        public description?: string,
        public lecturer?: string,
        public location?: string,
        public date?: Moment
    ) {}
}
