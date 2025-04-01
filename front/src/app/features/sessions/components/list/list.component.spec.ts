import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { ListComponent } from './list.component';
import { SessionService } from 'src/app/services/session.service';

describe('ListComponent - Minimal', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: { admin: true }
  };

  const dummySessions = [
    {
      id: 1,
      name: 'Yoga for Beginners',
      teacher_id: 10,
      date: new Date('2025-04-01T10:00:00'),
      description: 'A beginner-friendly yoga session.',
      users: [1, 2]
    },
    {
      id: 2,
      name: 'Advanced Yoga',
      teacher_id: 11,
      date: new Date('2025-04-02T11:00:00'),
      description: 'A challenging session for advanced practitioners.',
      users: [3]
    }
  ];

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
      declarations: [ListComponent],
      schemas: [NO_ERRORS_SCHEMA] // Ignore les erreurs sur les composants inconnus.
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;

    Object.defineProperty(component, 'user', { value: { admin: true } });

    component.sessions$ = of(dummySessions);
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display header title "Rentals available"', () => {
    const headerTitle = fixture.nativeElement.querySelector('mat-card-title');
    expect(headerTitle.textContent).toContain('Rentals available');
  });

  it('should display a Create button if user is admin', () => {
    const buttons = Array.from(fixture.nativeElement.querySelectorAll('button')) as HTMLElement[];
    const createButton = buttons.find((btn: HTMLElement) =>
      (btn.textContent ?? '').includes('Create')
    );
    expect(createButton).toBeTruthy();
  });

  it('should render a session card for each session', () => {
    const sessionCards = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionCards.length).toBe(dummySessions.length);
  });

  it('should display session details correctly', () => {
    const sessionCards = fixture.nativeElement.querySelectorAll('.item');
    dummySessions.forEach((session, index) => {
      const card = sessionCards[index];
      expect(card.textContent ?? '').toContain(session.name);
      const expectedDate = session.date.toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' });
      expect(card.textContent ?? '').toContain(expectedDate);
      expect(card.textContent ?? '').toContain(session.description);
    });
  });

  it('should show Detail and Edit buttons on each session card if user is admin', () => {
    const sessionCards = fixture.nativeElement.querySelectorAll('.item');
    sessionCards.forEach((card: HTMLElement) => {
      const buttons = Array.from(card.querySelectorAll('button')) as HTMLElement[];
      const detailButton = buttons.find((btn: HTMLElement) =>
        (btn.textContent ?? '').includes('Detail')
      );
      const editButton = buttons.find((btn: HTMLElement) =>
        (btn.textContent ?? '').includes('Edit')
      );
      expect(detailButton).toBeTruthy();
      expect(editButton).toBeTruthy();
    });
  });
});
