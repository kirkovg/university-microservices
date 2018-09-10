import { ICourse } from 'app/shared/model/courseinfo/course.model';

export interface IProgram {
    id?: number;
    name?: string;
    description?: string;
    courses?: ICourse[];
}

export class Program implements IProgram {
    constructor(public id?: number, public name?: string, public description?: string, public courses?: ICourse[]) {}
}
