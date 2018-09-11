import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompletedcourse } from 'app/shared/model/gradespreview/completedcourse.model';

@Component({
    selector: 'jhi-completedcourse-detail',
    templateUrl: './completedcourse-detail.component.html'
})
export class CompletedcourseDetailComponent implements OnInit {
    completedcourse: ICompletedcourse;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ completedcourse }) => {
            this.completedcourse = completedcourse;
        });
    }

    previousState() {
        window.history.back();
    }
}
