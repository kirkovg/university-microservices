export interface IStudent {
    id?: number;
    firstName?: string;
    lastName?: string;
    email?: string;
    phoneNumber?: string;
    otherInfo?: string;
}

export class Student implements IStudent {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public phoneNumber?: string,
        public otherInfo?: string
    ) {}
}
