import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IScheduledclass } from 'app/shared/model/schedule/scheduledclass.model';

type EntityResponseType = HttpResponse<IScheduledclass>;
type EntityArrayResponseType = HttpResponse<IScheduledclass[]>;

@Injectable({ providedIn: 'root' })
export class ScheduledclassService {
    private resourceUrl = SERVER_API_URL + 'schedule/api/scheduledclasses';

    constructor(private http: HttpClient) {}

    create(scheduledclass: IScheduledclass): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(scheduledclass);
        return this.http
            .post<IScheduledclass>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(scheduledclass: IScheduledclass): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(scheduledclass);
        return this.http
            .put<IScheduledclass>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IScheduledclass>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IScheduledclass[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(scheduledclass: IScheduledclass): IScheduledclass {
        const copy: IScheduledclass = Object.assign({}, scheduledclass, {
            date: scheduledclass.date != null && scheduledclass.date.isValid() ? scheduledclass.date.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((scheduledclass: IScheduledclass) => {
            scheduledclass.date = scheduledclass.date != null ? moment(scheduledclass.date) : null;
        });
        return res;
    }
}
