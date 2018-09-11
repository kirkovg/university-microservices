import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IScheduledclass } from 'app/shared/model/officehours/scheduledclass.model';
import { ScheduledclassService } from './scheduledclass.service';

@Component({
    selector: 'jhi-scheduledclass-delete-dialog',
    templateUrl: './scheduledclass-delete-dialog.component.html'
})
export class ScheduledclassDeleteDialogComponent {
    scheduledclass: IScheduledclass;

    constructor(
        private scheduledclassService: ScheduledclassService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.scheduledclassService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'scheduledclassListModification',
                content: 'Deleted an scheduledclass'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-scheduledclass-delete-popup',
    template: ''
})
export class ScheduledclassDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ scheduledclass }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ScheduledclassDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.scheduledclass = scheduledclass;
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
