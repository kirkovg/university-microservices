import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICompletedcourse } from 'app/shared/model/gradespreview/completedcourse.model';
import { CompletedcourseService } from './completedcourse.service';

@Component({
    selector: 'jhi-completedcourse-update',
    templateUrl: './completedcourse-update.component.html'
})
export class CompletedcourseUpdateComponent implements OnInit {
    private _completedcourse: ICompletedcourse;
    isSaving: boolean;

    constructor(private completedcourseService: CompletedcourseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ completedcourse }) => {
            this.completedcourse = completedcourse;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.completedcourse.id !== undefined) {
            this.subscribeToSaveResponse(this.completedcourseService.update(this.completedcourse));
        } else {
            this.subscribeToSaveResponse(this.completedcourseService.create(this.completedcourse));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompletedcourse>>) {
        result.subscribe((res: HttpResponse<ICompletedcourse>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get completedcourse() {
        return this._completedcourse;
    }

    set completedcourse(completedcourse: ICompletedcourse) {
        this._completedcourse = completedcourse;
    }
}
