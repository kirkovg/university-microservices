import { IExam } from 'app/shared/model/exam/exam.model';

export interface IAppuser {
    id?: number;
    name?: string;
    email?: string;
    exams?: IExam[];
}

export class Appuser implements IAppuser {
    constructor(public id?: number, public name?: string, public email?: string, public exams?: IExam[]) {}
}
