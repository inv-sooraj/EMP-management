import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoblistEmplyrComponent } from './joblist-emplyr.component';

describe('JoblistEmplyrComponent', () => {
  let component: JoblistEmplyrComponent;
  let fixture: ComponentFixture<JoblistEmplyrComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JoblistEmplyrComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JoblistEmplyrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
