import { ITask } from 'app/shared/model/assignedtask/task.model';

export interface IStudent {
    id?: number;
    name?: string;
    email?: string;
    tasks?: ITask[];
}

export class Student implements IStudent {
    constructor(public id?: number, public name?: string, public email?: string, public tasks?: ITask[]) {}
}
