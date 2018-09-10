export interface ICourse {
    id?: number;
    name?: string;
    description?: string;
    lecturer?: string;
}

export class Course implements ICourse {
    constructor(public id?: number, public name?: string, public description?: string, public lecturer?: string) {}
}
