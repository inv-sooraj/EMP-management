import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListJobRequestComponent } from './list-job-request.component';

describe('ListJobRequestComponent', () => {
  let component: ListJobRequestComponent;
  let fixture: ComponentFixture<ListJobRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListJobRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListJobRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
