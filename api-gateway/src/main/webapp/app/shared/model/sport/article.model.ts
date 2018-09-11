export interface IArticle {
    id?: number;
    name?: string;
    description?: string;
}

export class Article implements IArticle {
    constructor(public id?: number, public name?: string, public description?: string) {}
}
