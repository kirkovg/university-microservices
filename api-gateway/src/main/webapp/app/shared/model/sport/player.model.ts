export interface IPlayer {
    id?: number;
    name?: string;
    age?: number;
    weight?: number;
    height?: number;
}

export class Player implements IPlayer {
    constructor(public id?: number, public name?: string, public age?: number, public weight?: number, public height?: number) {}
}
