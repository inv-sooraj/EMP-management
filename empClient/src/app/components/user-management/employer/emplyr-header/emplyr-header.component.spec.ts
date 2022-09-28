import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmplyrHeaderComponent } from './emplyr-header.component';

describe('EmplyrHeaderComponent', () => {
  let component: EmplyrHeaderComponent;
  let fixture: ComponentFixture<EmplyrHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmplyrHeaderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmplyrHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
