import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppuser } from 'app/shared/model/exam/appuser.model';

@Component({
    selector: 'jhi-appuser-detail',
    templateUrl: './appuser-detail.component.html'
})
export class AppuserDetailComponent implements OnInit {
    appuser: IAppuser;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ appuser }) => {
            this.appuser = appuser;
        });
    }

    previousState() {
        window.history.back();
    }
}
