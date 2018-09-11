import { Moment } from 'moment';

export interface IArticle {
    id?: number;
    name?: string;
    content?: string;
    creationDate?: Moment;
    modificationDate?: Moment;
}

export class Article implements IArticle {
    constructor(
        public id?: number,
        public name?: string,
        public content?: string,
        public creationDate?: Moment,
        public modificationDate?: Moment
    ) {}
}
