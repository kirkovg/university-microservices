/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { ContactUpdateComponent } from 'app/entities/directory/contact/contact-update.component';
import { ContactService } from 'app/entities/directory/contact/contact.service';
import { Contact } from 'app/shared/model/directory/contact.model';

describe('Component Tests', () => {
    describe('Contact Management Update Component', () => {
        let comp: ContactUpdateComponent;
        let fixture: ComponentFixture<ContactUpdateComponent>;
        let service: ContactService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [ContactUpdateComponent]
            })
                .overrideTemplate(ContactUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ContactUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Contact(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.contact = entity;
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
                    const entity = new Contact();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.contact = entity;
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