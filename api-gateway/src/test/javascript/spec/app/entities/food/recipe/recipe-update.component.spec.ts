/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { RecipeUpdateComponent } from 'app/entities/food/recipe/recipe-update.component';
import { RecipeService } from 'app/entities/food/recipe/recipe.service';
import { Recipe } from 'app/shared/model/food/recipe.model';

describe('Component Tests', () => {
    describe('Recipe Management Update Component', () => {
        let comp: RecipeUpdateComponent;
        let fixture: ComponentFixture<RecipeUpdateComponent>;
        let service: RecipeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [RecipeUpdateComponent]
            })
                .overrideTemplate(RecipeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RecipeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RecipeService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Recipe(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.recipe = entity;
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
                    const entity = new Recipe();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.recipe = entity;
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
