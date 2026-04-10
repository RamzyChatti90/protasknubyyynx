import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStatus, NewStatus } from '../status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatus for edit and NewStatusFormGroupInput for create.
 */
type StatusFormGroupInput = IStatus | PartialWithRequiredKeyOf<NewStatus>;

type StatusFormDefaults = Pick<NewStatus, 'id'>;

type StatusFormGroupContent = {
  id: FormControl<IStatus['id'] | NewStatus['id']>;
  name: FormControl<IStatus['name']>;
};

export type StatusFormGroup = FormGroup<StatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StatusFormService {
  createStatusFormGroup(status: StatusFormGroupInput = { id: null }): StatusFormGroup {
    const statusRawValue = {
      ...this.getFormDefaults(),
      ...status,
    };
    return new FormGroup<StatusFormGroupContent>({
      id: new FormControl(
        { value: statusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(statusRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getStatus(form: StatusFormGroup): IStatus | NewStatus {
    return form.getRawValue() as IStatus | NewStatus;
  }

  resetForm(form: StatusFormGroup, status: StatusFormGroupInput): void {
    const statusRawValue = { ...this.getFormDefaults(), ...status };
    form.reset(
      {
        ...statusRawValue,
        id: { value: statusRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StatusFormDefaults {
    return {
      id: null,
    };
  }
}
