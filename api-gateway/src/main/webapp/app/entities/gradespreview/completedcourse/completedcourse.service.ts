import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompletedcourse } from 'app/shared/model/gradespreview/completedcourse.model';

type EntityResponseType = HttpResponse<ICompletedcourse>;
type EntityArrayResponseType = HttpResponse<ICompletedcourse[]>;

@Injectable({ providedIn: 'root' })
export class CompletedcourseService {
    private resourceUrl = SERVER_API_URL + 'gradespreview/api/completedcourses';

    constructor(private http: HttpClient) {}

    create(completedcourse: ICompletedcourse): Observable<EntityResponseType> {
        return this.http.post<ICompletedcourse>(this.resourceUrl, completedcourse, { observe: 'response' });
    }

    update(completedcourse: ICompletedcourse): Observable<EntityResponseType> {
        return this.http.put<ICompletedcourse>(this.resourceUrl, completedcourse, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICompletedcourse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompletedcourse[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
