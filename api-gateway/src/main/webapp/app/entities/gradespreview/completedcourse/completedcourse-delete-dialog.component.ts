import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICompletedcourse } from 'app/shared/model/gradespreview/completedcourse.model';
import { CompletedcourseService } from './completedcourse.service';

@Component({
    selector: 'jhi-completedcourse-delete-dialog',
    templateUrl: './completedcourse-delete-dialog.component.html'
})
export class CompletedcourseDeleteDialogComponent {
    completedcourse: ICompletedcourse;

    constructor(
        private completedcourseService: CompletedcourseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.completedcourseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'completedcourseListModification',
                content: 'Deleted an completedcourse'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-completedcourse-delete-popup',
    template: ''
})
export class CompletedcourseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ completedcourse }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CompletedcourseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.completedcourse = completedcourse;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
