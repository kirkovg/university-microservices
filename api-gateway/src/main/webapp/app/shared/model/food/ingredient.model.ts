export interface IIngredient {
    id?: number;
    name?: string;
    weightInGrams?: number;
}

export class Ingredient implements IIngredient {
    constructor(public id?: number, public name?: string, public weightInGrams?: number) {}
}
