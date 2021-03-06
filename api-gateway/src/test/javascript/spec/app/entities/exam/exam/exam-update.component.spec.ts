/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { ExamUpdateComponent } from 'app/entities/exam/exam/exam-update.component';
import { ExamService } from 'app/entities/exam/exam/exam.service';
import { Exam } from 'app/shared/model/exam/exam.model';

describe('Component Tests', () => {
    describe('Exam Management Update Component', () => {
        let comp: ExamUpdateComponent;
        let fixture: ComponentFixture<ExamUpdateComponent>;
        let service: ExamService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [ExamUpdateComponent]
            })
                .overrideTemplate(ExamUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ExamUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExamService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Exam(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.exam = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Exam();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.exam = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
