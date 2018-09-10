export interface ICourse {
    id?: number;
    name?: string;
    credits?: number;
    description?: string;
    professor?: string;
}

export class Course implements ICourse {
    constructor(
        public id?: number,
        public name?: string,
        public credits?: number,
        public description?: string,
        public professor?: string
    ) {}
}
