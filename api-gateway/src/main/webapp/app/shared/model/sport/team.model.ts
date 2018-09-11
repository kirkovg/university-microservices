import { IPlayer } from 'app/shared/model/sport/player.model';

export const enum SportType {
    FOOTBALL = 'FOOTBALL',
    BASKETBALL = 'BASKETBALL',
    VOLLEYBALL = 'VOLLEYBALL',
    ROWING = 'ROWING',
    RUGBY = 'RUGBY'
}

export interface ITeam {
    id?: number;
    name?: string;
    sportType?: SportType;
    players?: IPlayer[];
}

export class Team implements ITeam {
    constructor(public id?: number, public name?: string, public sportType?: SportType, public players?: IPlayer[]) {}
}
