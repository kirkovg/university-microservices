/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ApigatewayTestModule } from '../../../../test.module';
import { AppuserDeleteDialogComponent } from 'app/entities/communication/appuser/appuser-delete-dialog.component';
import { AppuserService } from 'app/entities/communication/appuser/appuser.service';

describe('Component Tests', () => {
    describe('Appuser Management Delete Component', () => {
        let comp: AppuserDeleteDialogComponent;
        let fixture: ComponentFixture<AppuserDeleteDialogComponent>;
        let service: AppuserService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [AppuserDeleteDialogComponent]
            })
                .overrideTemplate(AppuserDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AppuserDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppuserService);
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
