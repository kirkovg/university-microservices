import { ICompletedcourse } from 'app/shared/model/gradespreview/completedcourse.model';

export interface IStudent {
    id?: number;
    name?: string;
    email?: string;
    completedCourses?: ICompletedcourse[];
}

export class Student implements IStudent {
    constructor(public id?: number, public name?: string, public email?: string, public completedCourses?: ICompletedcourse[]) {}
}
