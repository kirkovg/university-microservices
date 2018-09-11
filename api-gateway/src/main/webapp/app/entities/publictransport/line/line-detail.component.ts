import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILine } from 'app/shared/model/publictransport/line.model';

@Component({
    selector: 'jhi-line-detail',
    templateUrl: './line-detail.component.html'
})
export class LineDetailComponent implements OnInit {
    line: ILine;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ line }) => {
            this.line = line;
        });
    }

    previousState() {
        window.history.back();
    }
}
