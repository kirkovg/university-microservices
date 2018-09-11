export interface IPlace {
    id?: number;
    name?: string;
    addressLine?: string;
    posX?: number;
    posY?: number;
}

export class Place implements IPlace {
    constructor(public id?: number, public name?: string, public addressLine?: string, public posX?: number, public posY?: number) {}
}
