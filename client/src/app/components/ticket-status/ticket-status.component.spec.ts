import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketStatusComponent } from './ticket-status.component';

describe('TicketStatusComponent', () => {
  let component: TicketStatusComponent;
  let fixture: ComponentFixture<TicketStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketStatusComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
