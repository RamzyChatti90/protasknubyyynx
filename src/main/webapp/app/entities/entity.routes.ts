import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'protasknubyyynxApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'project',
    data: { pageTitle: 'protasknubyyynxApp.project.home.title' },
    loadChildren: () => import('./project/project.routes'),
  },
  {
    path: 'task',
    data: { pageTitle: 'protasknubyyynxApp.task.home.title' },
    loadChildren: () => import('./task/task.routes'),
  },
  {
    path: 'status',
    data: { pageTitle: 'protasknubyyynxApp.status.home.title' },
    loadChildren: () => import('./status/status.routes'),
  },
  {
    path: 'priority',
    data: { pageTitle: 'protasknubyyynxApp.priority.home.title' },
    loadChildren: () => import('./priority/priority.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
