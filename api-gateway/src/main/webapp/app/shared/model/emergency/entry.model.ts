export interface IEntry {
    id?: number;
    name?: string;
    contact?: string;
}

export class Entry implements IEntry {
    constructor(public id?: number, public name?: string, public contact?: string) {}
}
