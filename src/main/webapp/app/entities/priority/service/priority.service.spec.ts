import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPriority } from '../priority.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../priority.test-samples';

import { PriorityService } from './priority.service';

const requireRestSample: IPriority = {
  ...sampleWithRequiredData,
};

describe('Priority Service', () => {
  let service: PriorityService;
  let httpMock: HttpTestingController;
  let expectedResult: IPriority | IPriority[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PriorityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Priority', () => {
      const priority = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(priority).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Priority', () => {
      const priority = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(priority).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Priority', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Priority', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Priority', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPriorityToCollectionIfMissing', () => {
      it('should add a Priority to an empty array', () => {
        const priority: IPriority = sampleWithRequiredData;
        expectedResult = service.addPriorityToCollectionIfMissing([], priority);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priority);
      });

      it('should not add a Priority to an array that contains it', () => {
        const priority: IPriority = sampleWithRequiredData;
        const priorityCollection: IPriority[] = [
          {
            ...priority,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPriorityToCollectionIfMissing(priorityCollection, priority);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Priority to an array that doesn't contain it", () => {
        const priority: IPriority = sampleWithRequiredData;
        const priorityCollection: IPriority[] = [sampleWithPartialData];
        expectedResult = service.addPriorityToCollectionIfMissing(priorityCollection, priority);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priority);
      });

      it('should add only unique Priority to an array', () => {
        const priorityArray: IPriority[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const priorityCollection: IPriority[] = [sampleWithRequiredData];
        expectedResult = service.addPriorityToCollectionIfMissing(priorityCollection, ...priorityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const priority: IPriority = sampleWithRequiredData;
        const priority2: IPriority = sampleWithPartialData;
        expectedResult = service.addPriorityToCollectionIfMissing([], priority, priority2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priority);
        expect(expectedResult).toContain(priority2);
      });

      it('should accept null and undefined values', () => {
        const priority: IPriority = sampleWithRequiredData;
        expectedResult = service.addPriorityToCollectionIfMissing([], null, priority, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priority);
      });

      it('should return initial array if no Priority is added', () => {
        const priorityCollection: IPriority[] = [sampleWithRequiredData];
        expectedResult = service.addPriorityToCollectionIfMissing(priorityCollection, undefined, null);
        expect(expectedResult).toEqual(priorityCollection);
      });
    });

    describe('comparePriority', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePriority(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1970 };
        const entity2 = null;

        const compareResult1 = service.comparePriority(entity1, entity2);
        const compareResult2 = service.comparePriority(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1970 };
        const entity2 = { id: 19881 };

        const compareResult1 = service.comparePriority(entity1, entity2);
        const compareResult2 = service.comparePriority(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1970 };
        const entity2 = { id: 1970 };

        const compareResult1 = service.comparePriority(entity1, entity2);
        const compareResult2 = service.comparePriority(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
