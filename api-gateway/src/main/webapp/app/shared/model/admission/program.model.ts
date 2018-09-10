import { IDocument } from 'app/shared/model/admission/document.model';

export interface IProgram {
    id?: number;
    name?: string;
    description?: string;
    lengthInYears?: number;
    title?: string;
    documents?: IDocument[];
}

export class Program implements IProgram {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public lengthInYears?: number,
        public title?: string,
        public documents?: IDocument[]
    ) {}
}
