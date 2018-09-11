export interface IBook {
    id?: number;
    name?: string;
    author?: string;
    available?: boolean;
}

export class Book implements IBook {
    constructor(public id?: number, public name?: string, public author?: string, public available?: boolean) {
        this.available = this.available || false;
    }
}
