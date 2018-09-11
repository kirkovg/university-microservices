export interface ICompletedcourse {
    id?: number;
    name?: string;
    grade?: number;
}

export class Completedcourse implements ICompletedcourse {
    constructor(public id?: number, public name?: string, public grade?: number) {}
}
