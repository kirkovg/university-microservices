import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApigatewaySharedModule } from 'app/shared';
import {
    AppuserComponent,
    AppuserDetailComponent,
    AppuserUpdateComponent,
    AppuserDeletePopupComponent,
    AppuserDeleteDialogComponent,
    appuserRoute,
    appuserPopupRoute
} from './';

const ENTITY_STATES = [...appuserRoute, ...appuserPopupRoute];

@NgModule({
    imports: [ApigatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AppuserComponent,
        AppuserDetailComponent,
        AppuserUpdateComponent,
        AppuserDeleteDialogComponent,
        AppuserDeletePopupComponent
    ],
    entryComponents: [AppuserComponent, AppuserUpdateComponent, AppuserDeleteDialogComponent, AppuserDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApigatewayAppuserModule {}
