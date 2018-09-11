import { Moment } from 'moment';

export const enum WeekDay {
    MONDAY = 'MONDAY',
    TUESDAY = 'TUESDAY',
    WEDNESDAY = 'WEDNESDAY',
    THURSDAY = 'THURSDAY',
    FRIDAY = 'FRIDAY',
    SATURDAY = 'SATURDAY',
    SUNDAY = 'SUNDAY'
}

export interface IScheduledclass {
    id?: number;
    name?: string;
    description?: string;
    lecturer?: string;
    location?: string;
    day?: WeekDay;
    date?: Moment;
}

export class Scheduledclass implements IScheduledclass {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public lecturer?: string,
        public location?: string,
        public day?: WeekDay,
        public date?: Moment
    ) {}
}
