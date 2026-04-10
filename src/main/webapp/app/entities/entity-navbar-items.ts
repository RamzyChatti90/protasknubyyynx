import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Project',
    route: '/project',
    translationKey: 'global.menu.entities.project',
  },
  {
    name: 'Task',
    route: '/task',
    translationKey: 'global.menu.entities.task',
  },
  {
    name: 'Status',
    route: '/status',
    translationKey: 'global.menu.entities.status',
  },
  {
    name: 'Priority',
    route: '/priority',
    translationKey: 'global.menu.entities.priority',
  },
];
