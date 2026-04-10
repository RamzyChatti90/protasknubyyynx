import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StatusDetailComponent } from './status-detail.component';

describe('Status Management Detail Component', () => {
  let comp: StatusDetailComponent;
  let fixture: ComponentFixture<StatusDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatusDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./status-detail.component').then(m => m.StatusDetailComponent),
              resolve: { status: () => of({ id: 2927 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StatusDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load status on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StatusDetailComponent);

      // THEN
      expect(instance.status()).toEqual(expect.objectContaining({ id: 2927 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
