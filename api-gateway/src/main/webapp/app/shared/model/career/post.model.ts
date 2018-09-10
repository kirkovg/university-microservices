import { Moment } from 'moment';

export const enum JobType {
    FULL_TIME_JOB = 'FULL_TIME_JOB',
    HALF_TIME_JOB = 'HALF_TIME_JOB',
    INTERNSHIP = 'INTERNSHIP',
    VOLUNTEER = 'VOLUNTEER'
}

export interface IPost {
    id?: number;
    title?: string;
    description?: string;
    creationDate?: Moment;
    dueDate?: Moment;
    jobType?: JobType;
    careerType?: string;
}

export class Post implements IPost {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public creationDate?: Moment,
        public dueDate?: Moment,
        public jobType?: JobType,
        public careerType?: string
    ) {}
}
