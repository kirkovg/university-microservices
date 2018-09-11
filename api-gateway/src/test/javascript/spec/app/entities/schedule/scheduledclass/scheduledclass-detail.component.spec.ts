/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { ScheduledclassDetailComponent } from 'app/entities/schedule/scheduledclass/scheduledclass-detail.component';
import { Scheduledclass } from 'app/shared/model/schedule/scheduledclass.model';

describe('Component Tests', () => {
    describe('Scheduledclass Management Detail Component', () => {
        let comp: ScheduledclassDetailComponent;
        let fixture: ComponentFixture<ScheduledclassDetailComponent>;
        const route = ({ data: of({ scheduledclass: new Scheduledclass(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [ScheduledclassDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ScheduledclassDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ScheduledclassDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.scheduledclass).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
