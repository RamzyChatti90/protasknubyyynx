import dayjs from 'dayjs/esm';

import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 9181,
  title: 'or consequently',
};

export const sampleWithPartialData: ITask = {
  id: 15218,
  title: 'gah worth',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ITask = {
  id: 13396,
  title: 'where wherever',
  description: '../fake-data/blob/hipster.txt',
  dueDate: dayjs('2026-04-10'),
  completed: false,
};

export const sampleWithNewData: NewTask = {
  title: 'expostulate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
