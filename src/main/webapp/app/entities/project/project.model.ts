import dayjs from 'dayjs/esm';
import { IStatus } from 'app/entities/status/status.model';

export interface IProject {
  id: number;
  name?: string | null;
  description?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: Pick<IStatus, 'id'> | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
