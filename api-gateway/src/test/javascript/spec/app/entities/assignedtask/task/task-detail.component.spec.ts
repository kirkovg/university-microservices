/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { TaskDetailComponent } from 'app/entities/assignedtask/task/task-detail.component';
import { Task } from 'app/shared/model/assignedtask/task.model';

describe('Component Tests', () => {
    describe('Task Management Detail Component', () => {
        let comp: TaskDetailComponent;
        let fixture: ComponentFixture<TaskDetailComponent>;
        const route = ({ data: of({ task: new Task(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [TaskDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TaskDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TaskDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.task).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
