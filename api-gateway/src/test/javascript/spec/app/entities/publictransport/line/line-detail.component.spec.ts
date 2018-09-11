/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { LineDetailComponent } from 'app/entities/publictransport/line/line-detail.component';
import { Line } from 'app/shared/model/publictransport/line.model';

describe('Component Tests', () => {
    describe('Line Management Detail Component', () => {
        let comp: LineDetailComponent;
        let fixture: ComponentFixture<LineDetailComponent>;
        const route = ({ data: of({ line: new Line(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [LineDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(LineDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LineDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.line).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
