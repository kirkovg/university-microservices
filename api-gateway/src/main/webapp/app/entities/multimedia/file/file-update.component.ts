import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IFile } from 'app/shared/model/multimedia/file.model';
import { FileService } from './file.service';

@Component({
    selector: 'jhi-file-update',
    templateUrl: './file-update.component.html'
})
export class FileUpdateComponent implements OnInit {
    private _file: IFile;
    isSaving: boolean;

    constructor(private dataUtils: JhiDataUtils, private fileService: FileService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ file }) => {
            this.file = file;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.file.id !== undefined) {
            this.subscribeToSaveResponse(this.fileService.update(this.file));
        } else {
            this.subscribeToSaveResponse(this.fileService.create(this.file));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFile>>) {
        result.subscribe((res: HttpResponse<IFile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get file() {
        return this._file;
    }

    set file(file: IFile) {
        this._file = file;
    }
}
