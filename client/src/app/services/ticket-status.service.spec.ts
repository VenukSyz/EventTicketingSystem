import { TestBed } from '@angular/core/testing';

import { TicketStatusService } from './ticket-status.service';

describe('TicketStatusService', () => {
  let service: TicketStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketStatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
