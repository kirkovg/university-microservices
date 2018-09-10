export interface IAppuser {
    id?: number;
    name?: string;
}

export class Appuser implements IAppuser {
    constructor(public id?: number, public name?: string) {}
}
