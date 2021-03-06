import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApigatewaySharedModule } from 'app/shared';
import {
    MenuComponent,
    MenuDetailComponent,
    MenuUpdateComponent,
    MenuDeletePopupComponent,
    MenuDeleteDialogComponent,
    menuRoute,
    menuPopupRoute
} from './';

const ENTITY_STATES = [...menuRoute, ...menuPopupRoute];

@NgModule({
    imports: [ApigatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [MenuComponent, MenuDetailComponent, MenuUpdateComponent, MenuDeleteDialogComponent, MenuDeletePopupComponent],
    entryComponents: [MenuComponent, MenuUpdateComponent, MenuDeleteDialogComponent, MenuDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApigatewayMenuModule {}
