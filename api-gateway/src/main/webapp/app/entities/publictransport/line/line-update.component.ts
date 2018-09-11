import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ILine } from 'app/shared/model/publictransport/line.model';
import { LineService } from './line.service';

@Component({
    selector: 'jhi-line-update',
    templateUrl: './line-update.component.html'
})
export class LineUpdateComponent implements OnInit {
    private _line: ILine;
    isSaving: boolean;
    universityStopTime: string;

    constructor(private lineService: LineService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ line }) => {
            this.line = line;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.line.universityStopTime = moment(this.universityStopTime, DATE_TIME_FORMAT);
        if (this.line.id !== undefined) {
            this.subscribeToSaveResponse(this.lineService.update(this.line));
        } else {
            this.subscribeToSaveResponse(this.lineService.create(this.line));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILine>>) {
        result.subscribe((res: HttpResponse<ILine>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get line() {
        return this._line;
    }

    set line(line: ILine) {
        this._line = line;
        this.universityStopTime = moment(line.universityStopTime).format(DATE_TIME_FORMAT);
    }
}
