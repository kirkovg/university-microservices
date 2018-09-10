import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITask } from 'app/shared/model/assignedtask/task.model';
import { TaskService } from './task.service';

@Component({
    selector: 'jhi-task-update',
    templateUrl: './task-update.component.html'
})
export class TaskUpdateComponent implements OnInit {
    private _task: ITask;
    isSaving: boolean;
    dueDate: string;

    constructor(private taskService: TaskService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ task }) => {
            this.task = task;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.task.dueDate = moment(this.dueDate, DATE_TIME_FORMAT);
        if (this.task.id !== undefined) {
            this.subscribeToSaveResponse(this.taskService.update(this.task));
        } else {
            this.subscribeToSaveResponse(this.taskService.create(this.task));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>) {
        result.subscribe((res: HttpResponse<ITask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get task() {
        return this._task;
    }

    set task(task: ITask) {
        this._task = task;
        this.dueDate = moment(task.dueDate).format(DATE_TIME_FORMAT);
    }
}
