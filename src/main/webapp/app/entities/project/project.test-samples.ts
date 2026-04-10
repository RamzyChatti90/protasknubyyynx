import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 22823,
  name: 'anxiously',
};

export const sampleWithPartialData: IProject = {
  id: 26103,
  name: 'yowza doubtfully',
  startDate: dayjs('2026-04-09'),
};

export const sampleWithFullData: IProject = {
  id: 1375,
  name: 'tray pack hydrolyze',
  description: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2026-04-10'),
  endDate: dayjs('2026-04-10'),
};

export const sampleWithNewData: NewProject = {
  name: 'zowie vice',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
