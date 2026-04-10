import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPriority, NewPriority } from '../priority.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPriority for edit and NewPriorityFormGroupInput for create.
 */
type PriorityFormGroupInput = IPriority | PartialWithRequiredKeyOf<NewPriority>;

type PriorityFormDefaults = Pick<NewPriority, 'id'>;

type PriorityFormGroupContent = {
  id: FormControl<IPriority['id'] | NewPriority['id']>;
  name: FormControl<IPriority['name']>;
};

export type PriorityFormGroup = FormGroup<PriorityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PriorityFormService {
  createPriorityFormGroup(priority: PriorityFormGroupInput = { id: null }): PriorityFormGroup {
    const priorityRawValue = {
      ...this.getFormDefaults(),
      ...priority,
    };
    return new FormGroup<PriorityFormGroupContent>({
      id: new FormControl(
        { value: priorityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(priorityRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getPriority(form: PriorityFormGroup): IPriority | NewPriority {
    return form.getRawValue() as IPriority | NewPriority;
  }

  resetForm(form: PriorityFormGroup, priority: PriorityFormGroupInput): void {
    const priorityRawValue = { ...this.getFormDefaults(), ...priority };
    form.reset(
      {
        ...priorityRawValue,
        id: { value: priorityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PriorityFormDefaults {
    return {
      id: null,
    };
  }
}
