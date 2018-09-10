/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApigatewayTestModule } from '../../../../test.module';
import { IngredientDetailComponent } from 'app/entities/food/ingredient/ingredient-detail.component';
import { Ingredient } from 'app/shared/model/food/ingredient.model';

describe('Component Tests', () => {
    describe('Ingredient Management Detail Component', () => {
        let comp: IngredientDetailComponent;
        let fixture: ComponentFixture<IngredientDetailComponent>;
        const route = ({ data: of({ ingredient: new Ingredient(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ApigatewayTestModule],
                declarations: [IngredientDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(IngredientDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IngredientDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.ingredient).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
