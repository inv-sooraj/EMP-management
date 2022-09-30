import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobRequestViewComponent } from './job-request-view.component';

describe('JobRequestViewComponent', () => {
  let component: JobRequestViewComponent;
  let fixture: ComponentFixture<JobRequestViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JobRequestViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JobRequestViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
