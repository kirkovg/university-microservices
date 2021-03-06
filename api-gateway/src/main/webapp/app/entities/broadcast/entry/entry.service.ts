import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEntry } from 'app/shared/model/broadcast/entry.model';

type EntityResponseType = HttpResponse<IEntry>;
type EntityArrayResponseType = HttpResponse<IEntry[]>;

@Injectable({ providedIn: 'root' })
export class EntryService {
    private resourceUrl = SERVER_API_URL + 'broadcast/api/entries';

    constructor(private http: HttpClient) {}

    create(entry: IEntry): Observable<EntityResponseType> {
        return this.http.post<IEntry>(this.resourceUrl, entry, { observe: 'response' });
    }

    update(entry: IEntry): Observable<EntityResponseType> {
        return this.http.put<IEntry>(this.resourceUrl, entry, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEntry[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
