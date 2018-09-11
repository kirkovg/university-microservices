import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IArticle } from 'app/shared/model/news/article.model';

type EntityResponseType = HttpResponse<IArticle>;
type EntityArrayResponseType = HttpResponse<IArticle[]>;

@Injectable({ providedIn: 'root' })
export class ArticleService {
    private resourceUrl = SERVER_API_URL + 'news/api/articles';

    constructor(private http: HttpClient) {}

    create(article: IArticle): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(article);
        return this.http
            .post<IArticle>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(article: IArticle): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(article);
        return this.http
            .put<IArticle>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IArticle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArticle[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(article: IArticle): IArticle {
        const copy: IArticle = Object.assign({}, article, {
            creationDate: article.creationDate != null && article.creationDate.isValid() ? article.creationDate.format(DATE_FORMAT) : null,
            modificationDate:
                article.modificationDate != null && article.modificationDate.isValid() ? article.modificationDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        res.body.modificationDate = res.body.modificationDate != null ? moment(res.body.modificationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((article: IArticle) => {
            article.creationDate = article.creationDate != null ? moment(article.creationDate) : null;
            article.modificationDate = article.modificationDate != null ? moment(article.modificationDate) : null;
        });
        return res;
    }
}
