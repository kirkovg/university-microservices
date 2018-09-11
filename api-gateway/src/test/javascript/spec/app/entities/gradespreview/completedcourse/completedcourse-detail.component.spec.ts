/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { CompletedcourseDetailComponent } from 'app/entities/gradespreview/completedcourse/completedcourse-detail.component';
import { Completedcourse } from 'app/shared/model/gradespreview/completedcourse.model';

describe('Component Tests', () => {
    describe('Completedcourse Management Detail Component', () => {
        let comp: CompletedcourseDetailComponent;
        let fixture: ComponentFixture<CompletedcourseDetailComponent>;
        const route = ({ data: of({ completedcourse: new Completedcourse(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [CompletedcourseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CompletedcourseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CompletedcourseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.completedcourse).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
