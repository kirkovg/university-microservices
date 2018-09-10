export const enum UniversityRole {
    STUDENT = 'STUDENT',
    ACADEMIC = 'ACADEMIC'
}

export interface IContact {
    id?: number;
    name?: string;
    role?: UniversityRole;
    contactEmail?: string;
}

export class Contact implements IContact {
    constructor(public id?: number, public name?: string, public role?: UniversityRole, public contactEmail?: string) {}
}
