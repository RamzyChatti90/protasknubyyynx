import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriority, NewPriority } from '../priority.model';

export type PartialUpdatePriority = Partial<IPriority> & Pick<IPriority, 'id'>;

export type EntityResponseType = HttpResponse<IPriority>;
export type EntityArrayResponseType = HttpResponse<IPriority[]>;

@Injectable({ providedIn: 'root' })
export class PriorityService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/priorities');

  create(priority: NewPriority): Observable<EntityResponseType> {
    return this.http.post<IPriority>(this.resourceUrl, priority, { observe: 'response' });
  }

  update(priority: IPriority): Observable<EntityResponseType> {
    return this.http.put<IPriority>(`${this.resourceUrl}/${this.getPriorityIdentifier(priority)}`, priority, { observe: 'response' });
  }

  partialUpdate(priority: PartialUpdatePriority): Observable<EntityResponseType> {
    return this.http.patch<IPriority>(`${this.resourceUrl}/${this.getPriorityIdentifier(priority)}`, priority, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPriority>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriority[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPriorityIdentifier(priority: Pick<IPriority, 'id'>): number {
    return priority.id;
  }

  comparePriority(o1: Pick<IPriority, 'id'> | null, o2: Pick<IPriority, 'id'> | null): boolean {
    return o1 && o2 ? this.getPriorityIdentifier(o1) === this.getPriorityIdentifier(o2) : o1 === o2;
  }

  addPriorityToCollectionIfMissing<Type extends Pick<IPriority, 'id'>>(
    priorityCollection: Type[],
    ...prioritiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const priorities: Type[] = prioritiesToCheck.filter(isPresent);
    if (priorities.length > 0) {
      const priorityCollectionIdentifiers = priorityCollection.map(priorityItem => this.getPriorityIdentifier(priorityItem));
      const prioritiesToAdd = priorities.filter(priorityItem => {
        const priorityIdentifier = this.getPriorityIdentifier(priorityItem);
        if (priorityCollectionIdentifiers.includes(priorityIdentifier)) {
          return false;
        }
        priorityCollectionIdentifiers.push(priorityIdentifier);
        return true;
      });
      return [...prioritiesToAdd, ...priorityCollection];
    }
    return priorityCollection;
  }
}
