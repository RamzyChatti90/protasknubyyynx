import { IStatus, NewStatus } from './status.model';

export const sampleWithRequiredData: IStatus = {
  id: 20359,
  name: 'eke',
};

export const sampleWithPartialData: IStatus = {
  id: 25977,
  name: 'verbally whether powerfully',
};

export const sampleWithFullData: IStatus = {
  id: 26853,
  name: 'yearly given',
};

export const sampleWithNewData: NewStatus = {
  name: 'rationale wasteful beautifully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
