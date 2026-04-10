export interface IPriority {
  id: number;
  name?: string | null;
}

export type NewPriority = Omit<IPriority, 'id'> & { id: null };
