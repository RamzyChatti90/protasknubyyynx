import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPriority } from '../priority.model';
import { PriorityService } from '../service/priority.service';
import { PriorityFormGroup, PriorityFormService } from './priority-form.service';

@Component({
  selector: 'jhi-priority-update',
  templateUrl: './priority-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PriorityUpdateComponent implements OnInit {
  isSaving = false;
  priority: IPriority | null = null;

  protected priorityService = inject(PriorityService);
  protected priorityFormService = inject(PriorityFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PriorityFormGroup = this.priorityFormService.createPriorityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priority }) => {
      this.priority = priority;
      if (priority) {
        this.updateForm(priority);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priority = this.priorityFormService.getPriority(this.editForm);
    if (priority.id !== null) {
      this.subscribeToSaveResponse(this.priorityService.update(priority));
    } else {
      this.subscribeToSaveResponse(this.priorityService.create(priority));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriority>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(priority: IPriority): void {
    this.priority = priority;
    this.priorityFormService.resetForm(this.editForm, priority);
  }
}
