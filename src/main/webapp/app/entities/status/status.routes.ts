import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import StatusResolve from './route/status-routing-resolve.service';

const statusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/status.component').then(m => m.StatusComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/status-detail.component').then(m => m.StatusDetailComponent),
    resolve: {
      status: StatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/status-update.component').then(m => m.StatusUpdateComponent),
    resolve: {
      status: StatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/status-update.component').then(m => m.StatusUpdateComponent),
    resolve: {
      status: StatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default statusRoute;
