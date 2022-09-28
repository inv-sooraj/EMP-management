import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoblistEmployeeComponent } from './joblist-employee.component';

describe('JoblistEmployeeComponent', () => {
  let component: JoblistEmployeeComponent;
  let fixture: ComponentFixture<JoblistEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JoblistEmployeeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JoblistEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
