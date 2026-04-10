import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPriority } from '../priority.model';
import { PriorityService } from '../service/priority.service';

const priorityResolve = (route: ActivatedRouteSnapshot): Observable<null | IPriority> => {
  const id = route.params.id;
  if (id) {
    return inject(PriorityService)
      .find(id)
      .pipe(
        mergeMap((priority: HttpResponse<IPriority>) => {
          if (priority.body) {
            return of(priority.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default priorityResolve;
