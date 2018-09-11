/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { CompletedcourseUpdateComponent } from 'app/entities/gradespreview/completedcourse/completedcourse-update.component';
import { CompletedcourseService } from 'app/entities/gradespreview/completedcourse/completedcourse.service';
import { Completedcourse } from 'app/shared/model/gradespreview/completedcourse.model';

describe('Component Tests', () => {
    describe('Completedcourse Management Update Component', () => {
        let comp: CompletedcourseUpdateComponent;
        let fixture: ComponentFixture<CompletedcourseUpdateComponent>;
        let service: CompletedcourseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [CompletedcourseUpdateComponent]
            })
                .overrideTemplate(CompletedcourseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CompletedcourseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompletedcourseService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Completedcourse(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.completedcourse = entity;
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
                    const entity = new Completedcourse();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.completedcourse = entity;
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
