import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PriorityResolve from './route/priority-routing-resolve.service';

const priorityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/priority.component').then(m => m.PriorityComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/priority-detail.component').then(m => m.PriorityDetailComponent),
    resolve: {
      priority: PriorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/priority-update.component').then(m => m.PriorityUpdateComponent),
    resolve: {
      priority: PriorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/priority-update.component').then(m => m.PriorityUpdateComponent),
    resolve: {
      priority: PriorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default priorityRoute;
