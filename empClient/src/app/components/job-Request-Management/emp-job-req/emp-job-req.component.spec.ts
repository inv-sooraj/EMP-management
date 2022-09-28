import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpJobReqComponent } from './emp-job-req.component';

describe('EmpJobReqComponent', () => {
  let component: EmpJobReqComponent;
  let fixture: ComponentFixture<EmpJobReqComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmpJobReqComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmpJobReqComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
