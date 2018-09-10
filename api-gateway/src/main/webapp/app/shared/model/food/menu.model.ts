import { IRecipe } from 'app/shared/model/food/recipe.model';

export interface IMenu {
    id?: number;
    name?: string;
    recipes?: IRecipe[];
}

export class Menu implements IMenu {
    constructor(public id?: number, public name?: string, public recipes?: IRecipe[]) {}
}
