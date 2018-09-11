import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IScheduledclass } from 'app/shared/model/officehours/scheduledclass.model';
import { ScheduledclassService } from './scheduledclass.service';

@Component({
    selector: 'jhi-scheduledclass-update',
    templateUrl: './scheduledclass-update.component.html'
})
export class ScheduledclassUpdateComponent implements OnInit {
    private _scheduledclass: IScheduledclass;
    isSaving: boolean;
    date: string;

    constructor(private scheduledclassService: ScheduledclassService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ scheduledclass }) => {
            this.scheduledclass = scheduledclass;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.scheduledclass.date = moment(this.date, DATE_TIME_FORMAT);
        if (this.scheduledclass.id !== undefined) {
            this.subscribeToSaveResponse(this.scheduledclassService.update(this.scheduledclass));
        } else {
            this.subscribeToSaveResponse(this.scheduledclassService.create(this.scheduledclass));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IScheduledclass>>) {
        result.subscribe((res: HttpResponse<IScheduledclass>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get scheduledclass() {
        return this._scheduledclass;
    }

    set scheduledclass(scheduledclass: IScheduledclass) {
        this._scheduledclass = scheduledclass;
        this.date = moment(scheduledclass.date).format(DATE_TIME_FORMAT);
    }
}
