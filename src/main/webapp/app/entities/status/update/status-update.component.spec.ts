import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { StatusService } from '../service/status.service';
import { IStatus } from '../status.model';
import { StatusFormService } from './status-form.service';

import { StatusUpdateComponent } from './status-update.component';

describe('Status Management Update Component', () => {
  let comp: StatusUpdateComponent;
  let fixture: ComponentFixture<StatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let statusFormService: StatusFormService;
  let statusService: StatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StatusUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(StatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusFormService = TestBed.inject(StatusFormService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const status: IStatus = { id: 22202 };

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(comp.status).toEqual(status);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatus>>();
      const status = { id: 2927 };
      jest.spyOn(statusFormService, 'getStatus').mockReturnValue(status);
      jest.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: status }));
      saveSubject.complete();

      // THEN
      expect(statusFormService.getStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusService.update).toHaveBeenCalledWith(expect.objectContaining(status));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatus>>();
      const status = { id: 2927 };
      jest.spyOn(statusFormService, 'getStatus').mockReturnValue({ id: null });
      jest.spyOn(statusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: status }));
      saveSubject.complete();

      // THEN
      expect(statusFormService.getStatus).toHaveBeenCalled();
      expect(statusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatus>>();
      const status = { id: 2927 };
      jest.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
