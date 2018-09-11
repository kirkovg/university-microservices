import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApigatewaySharedModule } from 'app/shared';
import {
    LineComponent,
    LineDetailComponent,
    LineUpdateComponent,
    LineDeletePopupComponent,
    LineDeleteDialogComponent,
    lineRoute,
    linePopupRoute
} from './';

const ENTITY_STATES = [...lineRoute, ...linePopupRoute];

@NgModule({
    imports: [ApigatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [LineComponent, LineDetailComponent, LineUpdateComponent, LineDeleteDialogComponent, LineDeletePopupComponent],
    entryComponents: [LineComponent, LineUpdateComponent, LineDeleteDialogComponent, LineDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApigatewayLineModule {}
