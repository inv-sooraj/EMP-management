import { TestBed } from '@angular/core/testing';

import { JobManagementService } from './job-management.service';

describe('JobManagementService', () => {
  let service: JobManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
