import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IStatus } from '../status.model';

@Component({
  selector: 'jhi-status-detail',
  templateUrl: './status-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class StatusDetailComponent {
  status = input<IStatus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
