import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAppuser } from 'app/shared/model/exam/appuser.model';
import { AppuserService } from './appuser.service';

@Component({
    selector: 'jhi-appuser-update',
    templateUrl: './appuser-update.component.html'
})
export class AppuserUpdateComponent implements OnInit {
    private _appuser: IAppuser;
    isSaving: boolean;

    constructor(private appuserService: AppuserService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ appuser }) => {
            this.appuser = appuser;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.appuser.id !== undefined) {
            this.subscribeToSaveResponse(this.appuserService.update(this.appuser));
        } else {
            this.subscribeToSaveResponse(this.appuserService.create(this.appuser));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAppuser>>) {
        result.subscribe((res: HttpResponse<IAppuser>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get appuser() {
        return this._appuser;
    }

    set appuser(appuser: IAppuser) {
        this._appuser = appuser;
    }
}
