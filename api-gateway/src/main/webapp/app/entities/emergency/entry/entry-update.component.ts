import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IEntry } from 'app/shared/model/emergency/entry.model';
import { EntryService } from './entry.service';

@Component({
    selector: 'jhi-entry-update',
    templateUrl: './entry-update.component.html'
})
export class EntryUpdateComponent implements OnInit {
    private _entry: IEntry;
    isSaving: boolean;

    constructor(private entryService: EntryService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ entry }) => {
            this.entry = entry;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.entry.id !== undefined) {
            this.subscribeToSaveResponse(this.entryService.update(this.entry));
        } else {
            this.subscribeToSaveResponse(this.entryService.create(this.entry));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEntry>>) {
        result.subscribe((res: HttpResponse<IEntry>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get entry() {
        return this._entry;
    }

    set entry(entry: IEntry) {
        this._entry = entry;
    }
}
