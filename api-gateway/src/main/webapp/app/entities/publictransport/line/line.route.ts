import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Line } from 'app/shared/model/publictransport/line.model';
import { LineService } from './line.service';
import { LineComponent } from './line.component';
import { LineDetailComponent } from './line-detail.component';
import { LineUpdateComponent } from './line-update.component';
import { LineDeletePopupComponent } from './line-delete-dialog.component';
import { ILine } from 'app/shared/model/publictransport/line.model';

@Injectable({ providedIn: 'root' })
export class LineResolve implements Resolve<ILine> {
    constructor(private service: LineService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((line: HttpResponse<Line>) => line.body));
        }
        return of(new Line());
    }
}

export const lineRoute: Routes = [
    {
        path: 'line',
        component: LineComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Lines'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'line/:id/view',
        component: LineDetailComponent,
        resolve: {
            line: LineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lines'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'line/new',
        component: LineUpdateComponent,
        resolve: {
            line: LineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lines'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'line/:id/edit',
        component: LineUpdateComponent,
        resolve: {
            line: LineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lines'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const linePopupRoute: Routes = [
    {
        path: 'line/:id/delete',
        component: LineDeletePopupComponent,
        resolve: {
            line: LineResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lines'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
