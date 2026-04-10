import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITask, NewTask } from '../task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITask for edit and NewTaskFormGroupInput for create.
 */
type TaskFormGroupInput = ITask | PartialWithRequiredKeyOf<NewTask>;

type TaskFormDefaults = Pick<NewTask, 'id' | 'completed'>;

type TaskFormGroupContent = {
  id: FormControl<ITask['id'] | NewTask['id']>;
  title: FormControl<ITask['title']>;
  description: FormControl<ITask['description']>;
  dueDate: FormControl<ITask['dueDate']>;
  completed: FormControl<ITask['completed']>;
  project: FormControl<ITask['project']>;
  status: FormControl<ITask['status']>;
  priority: FormControl<ITask['priority']>;
};

export type TaskFormGroup = FormGroup<TaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TaskFormService {
  createTaskFormGroup(task: TaskFormGroupInput = { id: null }): TaskFormGroup {
    const taskRawValue = {
      ...this.getFormDefaults(),
      ...task,
    };
    return new FormGroup<TaskFormGroupContent>({
      id: new FormControl(
        { value: taskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(taskRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(taskRawValue.description),
      dueDate: new FormControl(taskRawValue.dueDate),
      completed: new FormControl(taskRawValue.completed),
      project: new FormControl(taskRawValue.project),
      status: new FormControl(taskRawValue.status),
      priority: new FormControl(taskRawValue.priority),
    });
  }

  getTask(form: TaskFormGroup): ITask | NewTask {
    return form.getRawValue() as ITask | NewTask;
  }

  resetForm(form: TaskFormGroup, task: TaskFormGroupInput): void {
    const taskRawValue = { ...this.getFormDefaults(), ...task };
    form.reset(
      {
        ...taskRawValue,
        id: { value: taskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TaskFormDefaults {
    return {
      id: null,
      completed: false,
    };
  }
}
