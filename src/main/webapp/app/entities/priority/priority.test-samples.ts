import { IPriority, NewPriority } from './priority.model';

export const sampleWithRequiredData: IPriority = {
  id: 16659,
  name: 'inside before',
};

export const sampleWithPartialData: IPriority = {
  id: 14520,
  name: 'providence',
};

export const sampleWithFullData: IPriority = {
  id: 23995,
  name: 'overdue pish',
};

export const sampleWithNewData: NewPriority = {
  name: 'label',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
