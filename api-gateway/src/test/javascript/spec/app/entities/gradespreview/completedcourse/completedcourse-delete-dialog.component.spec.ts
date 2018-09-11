/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ApigatewayTestModule } from '../../../../test.module';
import { CompletedcourseDeleteDialogComponent } from 'app/entities/gradespreview/completedcourse/completedcourse-delete-dialog.component';
import { CompletedcourseService } from 'app/entities/gradespreview/completedcourse/completedcourse.service';

describe('Component Tests', () => {
    describe('Completedcourse Management Delete Component', () => {
        let comp: CompletedcourseDeleteDialogComponent;
        let fixture: ComponentFixture<CompletedcourseDeleteDialogComponent>;
        let service: CompletedcourseService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [CompletedcourseDeleteDialogComponent]
            })
                .overrideTemplate(CompletedcourseDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CompletedcourseDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompletedcourseService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
