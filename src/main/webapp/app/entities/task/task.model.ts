import dayjs from 'dayjs/esm';
import { IProject } from 'app/entities/project/project.model';
import { IStatus } from 'app/entities/status/status.model';
import { IPriority } from 'app/entities/priority/priority.model';

export interface ITask {
  id: number;
  title?: string | null;
  description?: string | null;
  dueDate?: dayjs.Dayjs | null;
  completed?: boolean | null;
  project?: Pick<IProject, 'id'> | null;
  status?: Pick<IStatus, 'id'> | null;
  priority?: Pick<IPriority, 'id'> | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
