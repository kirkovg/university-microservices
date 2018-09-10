import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IExam } from 'app/shared/model/exam/exam.model';
import { ExamService } from './exam.service';

@Component({
    selector: 'jhi-exam-update',
    templateUrl: './exam-update.component.html'
})
export class ExamUpdateComponent implements OnInit {
    private _exam: IExam;
    isSaving: boolean;
    time: string;

    constructor(private examService: ExamService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ exam }) => {
            this.exam = exam;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.exam.time = moment(this.time, DATE_TIME_FORMAT);
        if (this.exam.id !== undefined) {
            this.subscribeToSaveResponse(this.examService.update(this.exam));
        } else {
            this.subscribeToSaveResponse(this.examService.create(this.exam));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IExam>>) {
        result.subscribe((res: HttpResponse<IExam>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get exam() {
        return this._exam;
    }

    set exam(exam: IExam) {
        this._exam = exam;
        this.time = moment(exam.time).format(DATE_TIME_FORMAT);
    }
}
