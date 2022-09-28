import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmplyrJobReqtsComponent } from './emplyr-job-reqts.component';

describe('EmplyrJobReqtsComponent', () => {
  let component: EmplyrJobReqtsComponent;
  let fixture: ComponentFixture<EmplyrJobReqtsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmplyrJobReqtsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmplyrJobReqtsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
