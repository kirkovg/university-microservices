import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApigatewaySharedModule } from 'app/shared';
import {
    ScheduledclassComponent,
    ScheduledclassDetailComponent,
    ScheduledclassUpdateComponent,
    ScheduledclassDeletePopupComponent,
    ScheduledclassDeleteDialogComponent,
    scheduledclassRoute,
    scheduledclassPopupRoute
} from './';

const ENTITY_STATES = [...scheduledclassRoute, ...scheduledclassPopupRoute];

@NgModule({
    imports: [ApigatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ScheduledclassComponent,
        ScheduledclassDetailComponent,
        ScheduledclassUpdateComponent,
        ScheduledclassDeleteDialogComponent,
        ScheduledclassDeletePopupComponent
    ],
    entryComponents: [
        ScheduledclassComponent,
        ScheduledclassUpdateComponent,
        ScheduledclassDeleteDialogComponent,
        ScheduledclassDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApigatewayScheduledclassModule {}
