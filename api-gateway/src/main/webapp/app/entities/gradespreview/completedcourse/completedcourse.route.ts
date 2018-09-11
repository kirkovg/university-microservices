import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Completedcourse } from 'app/shared/model/gradespreview/completedcourse.model';
import { CompletedcourseService } from './completedcourse.service';
import { CompletedcourseComponent } from './completedcourse.component';
import { CompletedcourseDetailComponent } from './completedcourse-detail.component';
import { CompletedcourseUpdateComponent } from './completedcourse-update.component';
import { CompletedcourseDeletePopupComponent } from './completedcourse-delete-dialog.component';
import { ICompletedcourse } from 'app/shared/model/gradespreview/completedcourse.model';

@Injectable({ providedIn: 'root' })
export class CompletedcourseResolve implements Resolve<ICompletedcourse> {
    constructor(private service: CompletedcourseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((completedcourse: HttpResponse<Completedcourse>) => completedcourse.body));
        }
        return of(new Completedcourse());
    }
}

export const completedcourseRoute: Routes = [
    {
        path: 'completedcourse',
        component: CompletedcourseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Completedcourses'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'completedcourse/:id/view',
        component: CompletedcourseDetailComponent,
        resolve: {
            completedcourse: CompletedcourseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Completedcourses'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'completedcourse/new',
        component: CompletedcourseUpdateComponent,
        resolve: {
            completedcourse: CompletedcourseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Completedcourses'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'completedcourse/:id/edit',
        component: CompletedcourseUpdateComponent,
        resolve: {
            completedcourse: CompletedcourseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Completedcourses'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const completedcoursePopupRoute: Routes = [
    {
        path: 'completedcourse/:id/delete',
        component: CompletedcourseDeletePopupComponent,
        resolve: {
            completedcourse: CompletedcourseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Completedcourses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
