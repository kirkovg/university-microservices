import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Appuser } from 'app/shared/model/exam/appuser.model';
import { AppuserService } from './appuser.service';
import { AppuserComponent } from './appuser.component';
import { AppuserDetailComponent } from './appuser-detail.component';
import { AppuserUpdateComponent } from './appuser-update.component';
import { AppuserDeletePopupComponent } from './appuser-delete-dialog.component';
import { IAppuser } from 'app/shared/model/exam/appuser.model';

@Injectable({ providedIn: 'root' })
export class AppuserResolve implements Resolve<IAppuser> {
    constructor(private service: AppuserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((appuser: HttpResponse<Appuser>) => appuser.body));
        }
        return of(new Appuser());
    }
}

export const appuserRoute: Routes = [
    {
        path: 'appuser',
        component: AppuserComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Appusers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'appuser/:id/view',
        component: AppuserDetailComponent,
        resolve: {
            appuser: AppuserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appusers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'appuser/new',
        component: AppuserUpdateComponent,
        resolve: {
            appuser: AppuserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appusers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'appuser/:id/edit',
        component: AppuserUpdateComponent,
        resolve: {
            appuser: AppuserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appusers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const appuserPopupRoute: Routes = [
    {
        path: 'appuser/:id/delete',
        component: AppuserDeletePopupComponent,
        resolve: {
            appuser: AppuserResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appusers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
