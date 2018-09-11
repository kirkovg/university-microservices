import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IScheduledclass } from 'app/shared/model/schedule/scheduledclass.model';

@Component({
    selector: 'jhi-scheduledclass-detail',
    templateUrl: './scheduledclass-detail.component.html'
})
export class ScheduledclassDetailComponent implements OnInit {
    scheduledclass: IScheduledclass;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ scheduledclass }) => {
            this.scheduledclass = scheduledclass;
        });
    }

    previousState() {
        window.history.back();
    }
}
