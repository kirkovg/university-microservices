import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ApigatewayDocumentModule as AdmissionDocumentModule } from './admission/document/document.module';
import { ApigatewayProgramModule as AdmissionProgramModule } from './admission/program/program.module';
import { ApigatewayTaskModule as AssignedtaskTaskModule } from './assignedtask/task/task.module';
import { ApigatewayStudentModule as AssignedtaskStudentModule } from './assignedtask/student/student.module';
import { ApigatewayEntryModule as BroadcastEntryModule } from './broadcast/entry/entry.module';
import { ApigatewayPostModule as CareerPostModule } from './career/post/post.module';
import { ApigatewayAppuserModule as CommunicationAppuserModule } from './communication/appuser/appuser.module';
import { ApigatewayMessageModule as CommunicationMessageModule } from './communication/message/message.module';
import { ApigatewayStudentModule as ContactStudentModule } from './contact/student/student.module';
import { ApigatewayCourseModule as CourseinfoCourseModule } from './courseinfo/course/course.module';
import { ApigatewayProgramModule as CourseinfoProgramModule } from './courseinfo/program/program.module';
import { ApigatewayContactModule as DirectoryContactModule } from './directory/contact/contact.module';
import { ApigatewayEntryModule as EmergencyEntryModule } from './emergency/entry/entry.module';
import { ApigatewayCourseModule as EnrolledcoursesCourseModule } from './enrolledcourses/course/course.module';
import { ApigatewayStudentModule as EnrolledcoursesStudentModule } from './enrolledcourses/student/student.module';
import { ApigatewayExamModule as ExamExamModule } from './exam/exam/exam.module';
import { ApigatewayAppuserModule as ExamAppuserModule } from './exam/appuser/appuser.module';
import { ApigatewayIngredientModule as FoodIngredientModule } from './food/ingredient/ingredient.module';
import { ApigatewayRecipeModule as FoodRecipeModule } from './food/recipe/recipe.module';
import { ApigatewayMenuModule as FoodMenuModule } from './food/menu/menu.module';
import { ApigatewayRestaurantModule as FoodRestaurantModule } from './food/restaurant/restaurant.module';
import { ApigatewayCompletedcourseModule as GradespreviewCompletedcourseModule } from './gradespreview/completedcourse/completedcourse.module';
import { ApigatewayStudentModule as GradespreviewStudentModule } from './gradespreview/student/student.module';
import { ApigatewayBookModule as LibraryBookModule } from './library/book/book.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        AdmissionDocumentModule,
        AdmissionProgramModule,
        AssignedtaskTaskModule,
        AssignedtaskStudentModule,
        BroadcastEntryModule,
        CareerPostModule,
        CommunicationAppuserModule,
        CommunicationMessageModule,
        ContactStudentModule,
        CourseinfoCourseModule,
        CourseinfoProgramModule,
        DirectoryContactModule,
        EmergencyEntryModule,
        EnrolledcoursesCourseModule,
        EnrolledcoursesStudentModule,
        ExamExamModule,
        ExamAppuserModule,
        FoodIngredientModule,
        FoodRecipeModule,
        FoodMenuModule,
        FoodRestaurantModule,
        GradespreviewCompletedcourseModule,
        GradespreviewStudentModule,
        LibraryBookModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApigatewayEntityModule {}
