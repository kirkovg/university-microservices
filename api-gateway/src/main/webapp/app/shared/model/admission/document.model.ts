export interface IDocument {
    id?: number;
    name?: string;
    description?: string;
    fileContentType?: string;
    file?: any;
}

export class Document implements IDocument {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public fileContentType?: string,
        public file?: any
    ) {}
}
