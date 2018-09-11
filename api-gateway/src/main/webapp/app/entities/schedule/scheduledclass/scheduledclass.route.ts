import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Scheduledclass } from 'app/shared/model/schedule/scheduledclass.model';
import { ScheduledclassService } from './scheduledclass.service';
import { ScheduledclassComponent } from './scheduledclass.component';
import { ScheduledclassDetailComponent } from './scheduledclass-detail.component';
import { ScheduledclassUpdateComponent } from './scheduledclass-update.component';
import { ScheduledclassDeletePopupComponent } from './scheduledclass-delete-dialog.component';
import { IScheduledclass } from 'app/shared/model/schedule/scheduledclass.model';

@Injectable({ providedIn: 'root' })
export class ScheduledclassResolve implements Resolve<IScheduledclass> {
    constructor(private service: ScheduledclassService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((scheduledclass: HttpResponse<Scheduledclass>) => scheduledclass.body));
        }
        return of(new Scheduledclass());
    }
}

export const scheduledclassRoute: Routes = [
    {
        path: 'scheduledclass',
        component: ScheduledclassComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Scheduledclasses'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'scheduledclass/:id/view',
        component: ScheduledclassDetailComponent,
        resolve: {
            scheduledclass: ScheduledclassResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scheduledclasses'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'scheduledclass/new',
        component: ScheduledclassUpdateComponent,
        resolve: {
            scheduledclass: ScheduledclassResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scheduledclasses'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'scheduledclass/:id/edit',
        component: ScheduledclassUpdateComponent,
        resolve: {
            scheduledclass: ScheduledclassResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scheduledclasses'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const scheduledclassPopupRoute: Routes = [
    {
        path: 'scheduledclass/:id/delete',
        component: ScheduledclassDeletePopupComponent,
        resolve: {
            scheduledclass: ScheduledclassResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scheduledclasses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
