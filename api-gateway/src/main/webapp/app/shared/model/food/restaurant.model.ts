import { IMenu } from 'app/shared/model/food/menu.model';

export interface IRestaurant {
    id?: number;
    name?: string;
    location?: string;
    capacity?: number;
    menus?: IMenu[];
}

export class Restaurant implements IRestaurant {
    constructor(public id?: number, public name?: string, public location?: string, public capacity?: number, public menus?: IMenu[]) {}
}
