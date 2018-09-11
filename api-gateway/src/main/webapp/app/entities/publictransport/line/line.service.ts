import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILine } from 'app/shared/model/publictransport/line.model';

type EntityResponseType = HttpResponse<ILine>;
type EntityArrayResponseType = HttpResponse<ILine[]>;

@Injectable({ providedIn: 'root' })
export class LineService {
    private resourceUrl = SERVER_API_URL + 'publictransport/api/lines';

    constructor(private http: HttpClient) {}

    create(line: ILine): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(line);
        return this.http
            .post<ILine>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(line: ILine): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(line);
        return this.http
            .put<ILine>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ILine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ILine[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(line: ILine): ILine {
        const copy: ILine = Object.assign({}, line, {
            universityStopTime:
                line.universityStopTime != null && line.universityStopTime.isValid() ? line.universityStopTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.universityStopTime = res.body.universityStopTime != null ? moment(res.body.universityStopTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((line: ILine) => {
            line.universityStopTime = line.universityStopTime != null ? moment(line.universityStopTime) : null;
        });
        return res;
    }
}
