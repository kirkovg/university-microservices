/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { DocumentDetailComponent } from 'app/entities/admission/document/document-detail.component';
import { Document } from 'app/shared/model/admission/document.model';

describe('Component Tests', () => {
    describe('Document Management Detail Component', () => {
        let comp: DocumentDetailComponent;
        let fixture: ComponentFixture<DocumentDetailComponent>;
        const route = ({ data: of({ document: new Document(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [DocumentDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DocumentDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DocumentDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.document).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
