/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { ScheduledclassUpdateComponent } from 'app/entities/officehours/scheduledclass/scheduledclass-update.component';
import { ScheduledclassService } from 'app/entities/officehours/scheduledclass/scheduledclass.service';
import { Scheduledclass } from 'app/shared/model/officehours/scheduledclass.model';

describe('Component Tests', () => {
    describe('Scheduledclass Management Update Component', () => {
        let comp: ScheduledclassUpdateComponent;
        let fixture: ComponentFixture<ScheduledclassUpdateComponent>;
        let service: ScheduledclassService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [ScheduledclassUpdateComponent]
            })
                .overrideTemplate(ScheduledclassUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ScheduledclassUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScheduledclassService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Scheduledclass(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.scheduledclass = entity;
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
                    const entity = new Scheduledclass();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.scheduledclass = entity;
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
