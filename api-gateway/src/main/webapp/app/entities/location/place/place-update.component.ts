import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPlace } from 'app/shared/model/location/place.model';
import { PlaceService } from './place.service';

@Component({
    selector: 'jhi-place-update',
    templateUrl: './place-update.component.html'
})
export class PlaceUpdateComponent implements OnInit {
    private _place: IPlace;
    isSaving: boolean;

    constructor(private placeService: PlaceService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ place }) => {
            this.place = place;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.place.id !== undefined) {
            this.subscribeToSaveResponse(this.placeService.update(this.place));
        } else {
            this.subscribeToSaveResponse(this.placeService.create(this.place));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPlace>>) {
        result.subscribe((res: HttpResponse<IPlace>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get place() {
        return this._place;
    }

    set place(place: IPlace) {
        this._place = place;
    }
}
