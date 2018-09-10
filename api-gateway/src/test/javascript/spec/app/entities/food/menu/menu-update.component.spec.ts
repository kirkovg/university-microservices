/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { MenuUpdateComponent } from 'app/entities/food/menu/menu-update.component';
import { MenuService } from 'app/entities/food/menu/menu.service';
import { Menu } from 'app/shared/model/food/menu.model';

describe('Component Tests', () => {
    describe('Menu Management Update Component', () => {
        let comp: MenuUpdateComponent;
        let fixture: ComponentFixture<MenuUpdateComponent>;
        let service: MenuService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [MenuUpdateComponent]
            })
                .overrideTemplate(MenuUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MenuUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MenuService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Menu(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.menu = entity;
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
                    const entity = new Menu();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.menu = entity;
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
