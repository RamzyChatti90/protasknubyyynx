export interface IStatus {
  id: number;
  name?: string | null;
}

export type NewStatus = Omit<IStatus, 'id'> & { id: null };
