import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApigatewaySharedModule } from 'app/shared';
import {
    CompletedcourseComponent,
    CompletedcourseDetailComponent,
    CompletedcourseUpdateComponent,
    CompletedcourseDeletePopupComponent,
    CompletedcourseDeleteDialogComponent,
    completedcourseRoute,
    completedcoursePopupRoute
} from './';

const ENTITY_STATES = [...completedcourseRoute, ...completedcoursePopupRoute];

@NgModule({
    imports: [ApigatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CompletedcourseComponent,
        CompletedcourseDetailComponent,
        CompletedcourseUpdateComponent,
        CompletedcourseDeleteDialogComponent,
        CompletedcourseDeletePopupComponent
    ],
    entryComponents: [
        CompletedcourseComponent,
        CompletedcourseUpdateComponent,
        CompletedcourseDeleteDialogComponent,
        CompletedcourseDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApigatewayCompletedcourseModule {}
