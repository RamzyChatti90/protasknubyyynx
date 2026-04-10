import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IPriority } from 'app/entities/priority/priority.model';
import { PriorityService } from 'app/entities/priority/service/priority.service';
import { TaskService } from '../service/task.service';
import { ITask } from '../task.model';
import { TaskFormGroup, TaskFormService } from './task-form.service';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;
  task: ITask | null = null;

  projectsSharedCollection: IProject[] = [];
  statusesSharedCollection: IStatus[] = [];
  prioritiesSharedCollection: IPriority[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected taskService = inject(TaskService);
  protected taskFormService = inject(TaskFormService);
  protected projectService = inject(ProjectService);
  protected statusService = inject(StatusService);
  protected priorityService = inject(PriorityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TaskFormGroup = this.taskFormService.createTaskFormGroup();

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  comparePriority = (o1: IPriority | null, o2: IPriority | null): boolean => this.priorityService.comparePriority(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      this.task = task;
      if (task) {
        this.updateForm(task);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('protasknubyyynxApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.taskFormService.getTask(this.editForm);
    if (task.id !== null) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
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

  protected updateForm(task: ITask): void {
    this.task = task;
    this.taskFormService.resetForm(this.editForm, task);

    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      task.project,
    );
    this.statusesSharedCollection = this.statusService.addStatusToCollectionIfMissing<IStatus>(this.statusesSharedCollection, task.status);
    this.prioritiesSharedCollection = this.priorityService.addPriorityToCollectionIfMissing<IPriority>(
      this.prioritiesSharedCollection,
      task.priority,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.task?.project)))
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.task?.status)))
      .subscribe((statuses: IStatus[]) => (this.statusesSharedCollection = statuses));

    this.priorityService
      .query()
      .pipe(map((res: HttpResponse<IPriority[]>) => res.body ?? []))
      .pipe(
        map((priorities: IPriority[]) => this.priorityService.addPriorityToCollectionIfMissing<IPriority>(priorities, this.task?.priority)),
      )
      .subscribe((priorities: IPriority[]) => (this.prioritiesSharedCollection = priorities));
  }
}
