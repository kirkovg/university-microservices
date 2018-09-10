import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ApigatewayDocumentModule as AdmissionDocumentModule } from './admission/document/document.module';
import { ApigatewayProgramModule as AdmissionProgramModule } from './admission/program/program.module';
import { ApigatewayTaskModule as AssignedtaskTaskModule } from './assignedtask/task/task.module';
import { ApigatewayStudentModule as AssignedtaskStudentModule } from './assignedtask/student/student.module';
import { ApigatewayEntryModule as BroadcastEntryModule } from './broadcast/entry/entry.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        AdmissionDocumentModule,
        AdmissionProgramModule,
        AssignedtaskTaskModule,
        AssignedtaskStudentModule,
        BroadcastEntryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApigatewayEntityModule {}
