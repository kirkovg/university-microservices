export interface IFile {
    id?: number;
    name?: string;
    description?: string;
    fileContentType?: string;
    file?: any;
}

export class File implements IFile {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public fileContentType?: string,
        public file?: any
    ) {}
}
