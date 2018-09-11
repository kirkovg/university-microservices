export interface IPhoto {
    id?: number;
    name?: string;
    description?: string;
    pictureContentType?: string;
    picture?: any;
}

export class Photo implements IPhoto {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public pictureContentType?: string,
        public picture?: any
    ) {}
}
