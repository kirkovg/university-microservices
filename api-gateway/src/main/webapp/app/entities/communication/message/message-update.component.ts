import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IMessage } from 'app/shared/model/communication/message.model';
import { MessageService } from './message.service';
import { IAppuser } from 'app/shared/model/communication/appuser.model';
import { AppuserService } from 'app/entities/communication/appuser';

@Component({
    selector: 'jhi-message-update',
    templateUrl: './message-update.component.html'
})
export class MessageUpdateComponent implements OnInit {
    private _message: IMessage;
    isSaving: boolean;

    froms: IAppuser[];

    tos: IAppuser[];
    sentAt: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private messageService: MessageService,
        private appuserService: AppuserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ message }) => {
            this.message = message;
        });
        this.appuserService.query({ filter: 'message-is-null' }).subscribe(
            (res: HttpResponse<IAppuser[]>) => {
                if (!this.message.from || !this.message.from.id) {
                    this.froms = res.body;
                } else {
                    this.appuserService.find(this.message.from.id).subscribe(
                        (subRes: HttpResponse<IAppuser>) => {
                            this.froms = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.appuserService.query({ filter: 'message-is-null' }).subscribe(
            (res: HttpResponse<IAppuser[]>) => {
                if (!this.message.to || !this.message.to.id) {
                    this.tos = res.body;
                } else {
                    this.appuserService.find(this.message.to.id).subscribe(
                        (subRes: HttpResponse<IAppuser>) => {
                            this.tos = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.message.sentAt = moment(this.sentAt, DATE_TIME_FORMAT);
        if (this.message.id !== undefined) {
            this.subscribeToSaveResponse(this.messageService.update(this.message));
        } else {
            this.subscribeToSaveResponse(this.messageService.create(this.message));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMessage>>) {
        result.subscribe((res: HttpResponse<IMessage>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackAppuserById(index: number, item: IAppuser) {
        return item.id;
    }
    get message() {
        return this._message;
    }

    set message(message: IMessage) {
        this._message = message;
        this.sentAt = moment(message.sentAt).format(DATE_TIME_FORMAT);
    }
}
