import { TestBed } from '@angular/core/testing';

import { JobRequestManagementService } from './job-request-management.service';

describe('JobRequestManagementService', () => {
  let service: JobRequestManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobRequestManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
