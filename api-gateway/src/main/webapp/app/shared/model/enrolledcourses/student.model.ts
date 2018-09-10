import { ICourse } from 'app/shared/model/enrolledcourses/course.model';

export interface IStudent {
    id?: number;
    name?: string;
    email?: string;
    courses?: ICourse[];
}

export class Student implements IStudent {
    constructor(public id?: number, public name?: string, public email?: string, public courses?: ICourse[]) {}
}
