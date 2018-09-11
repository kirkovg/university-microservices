import { Moment } from 'moment';
import { ITeam } from 'app/shared/model/sport/team.model';

export interface ISchedule {
    id?: number;
    description?: string;
    scheduledTime?: Moment;
    teams?: ITeam[];
}

export class Schedule implements ISchedule {
    constructor(public id?: number, public description?: string, public scheduledTime?: Moment, public teams?: ITeam[]) {}
}
