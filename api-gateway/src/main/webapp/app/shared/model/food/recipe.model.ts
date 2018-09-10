import { IIngredient } from 'app/shared/model/food/ingredient.model';

export interface IRecipe {
    id?: number;
    name?: string;
    price?: number;
    ingredients?: IIngredient[];
}

export class Recipe implements IRecipe {
    constructor(public id?: number, public name?: string, public price?: number, public ingredients?: IIngredient[]) {}
}
