import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../priority.test-samples';

import { PriorityFormService } from './priority-form.service';

describe('Priority Form Service', () => {
  let service: PriorityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PriorityFormService);
  });

  describe('Service methods', () => {
    describe('createPriorityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPriorityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IPriority should create a new form with FormGroup', () => {
        const formGroup = service.createPriorityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getPriority', () => {
      it('should return NewPriority for default Priority initial value', () => {
        const formGroup = service.createPriorityFormGroup(sampleWithNewData);

        const priority = service.getPriority(formGroup) as any;

        expect(priority).toMatchObject(sampleWithNewData);
      });

      it('should return NewPriority for empty Priority initial value', () => {
        const formGroup = service.createPriorityFormGroup();

        const priority = service.getPriority(formGroup) as any;

        expect(priority).toMatchObject({});
      });

      it('should return IPriority', () => {
        const formGroup = service.createPriorityFormGroup(sampleWithRequiredData);

        const priority = service.getPriority(formGroup) as any;

        expect(priority).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPriority should not enable id FormControl', () => {
        const formGroup = service.createPriorityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPriority should disable id FormControl', () => {
        const formGroup = service.createPriorityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
