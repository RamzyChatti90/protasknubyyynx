import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPriority } from '../priority.model';

@Component({
  selector: 'jhi-priority-detail',
  templateUrl: './priority-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PriorityDetailComponent {
  priority = input<IPriority | null>(null);

  previousState(): void {
    window.history.back();
  }
}
