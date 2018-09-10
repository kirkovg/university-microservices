export interface IEntry {
    id?: number;
    name?: string;
    url?: string;
}

export class Entry implements IEntry {
    constructor(public id?: number, public name?: string, public url?: string) {}
}
