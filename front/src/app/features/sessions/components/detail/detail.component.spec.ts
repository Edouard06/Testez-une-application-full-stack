import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { expect } from '@jest/globals';

import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';

describe('DetailComponent - Improved', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionService: SessionService;

  // Mise à jour des mocks pour respecter l'interface Session.
  // Ici, "users" est un tableau de nombres (IDs) au lieu d'objets.
  const mockSession = {
    teacher_id: 10, // Propriété requise
    name: 'yoga session',
    description: 'Relaxing yoga session',
    date: new Date('2025-03-11T12:00:00'),
    createdAt: new Date('2025-03-10T12:00:00'),
    updatedAt: new Date('2025-03-11T08:00:00'),
    users: [1, 2]  // Tableau de nombres représentant les IDs des utilisateurs
  };

  // Mise à jour des mocks pour respecter l'interface Teacher.
  const mockTeacher = {
    id: 10, // Correspond à teacher_id de la session
    firstName: 'Alice',
    lastName: 'Smith',
    createdAt: new Date('2025-03-09T12:00:00'),
    updatedAt: new Date('2025-03-10T12:00:00')
  };

  // Mock minimal de SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);

    // Affecter les mocks au composant
    component.session = mockSession;
    component.teacher = mockTeacher;
    component.isAdmin = true;       // Par défaut, utilisateur admin
    component.isParticipate = false; // Par défaut, non participant

    // Espionner quelques méthodes pour tester les interactions
    jest.spyOn(component, 'back');
    jest.spyOn(component, 'delete');
    jest.spyOn(component, 'participate');
    jest.spyOn(component, 'unParticipate');

    fixture.detectChanges();
  });

  it('should create the detail component', () => {
    expect(component).toBeTruthy();
  });

  it('should display session title in mat-card-title in titlecase', () => {
    const compiled = fixture.nativeElement;
    const titleElement = compiled.querySelector('mat-card-title h1');
    // Vérifie que le titre est affiché en titlecase (ex. "Yoga Session")
    expect(titleElement.textContent).toContain('Yoga Session');
  });

  it('should display teacher information if teacher exists', () => {
    const compiled = fixture.nativeElement;
    const teacherInfo = compiled.querySelector('mat-card-subtitle');
    expect(teacherInfo).toBeTruthy();
    // Vérifie que le prénom et le nom (en majuscules) sont affichés
    expect(teacherInfo.textContent).toContain('Alice');
    expect(teacherInfo.textContent).toContain('SMITH');
  });

  it('should show delete button if user is admin', () => {
    const compiled = fixture.nativeElement;
    const deleteButton = compiled.querySelector('button[color="warn"]');
    expect(deleteButton).toBeTruthy();
    expect(deleteButton.textContent).toContain('Delete');
  });

  it('should show participate button if user is not admin and not participating', () => {
    component.isAdmin = false;
    component.isParticipate = false;
    fixture.detectChanges();

    const compiled = fixture.nativeElement;
    const participateButton = compiled.querySelector('button[color="primary"]');
    expect(participateButton).toBeTruthy();
    expect(participateButton.textContent).toContain('Participate');
  });

  it('should show unParticipate button if user is not admin and is participating', () => {
    component.isAdmin = false;
    component.isParticipate = true;
    fixture.detectChanges();

    const compiled = fixture.nativeElement;
    const unParticipateButton = compiled.querySelector('button[color="warn"]');
    expect(unParticipateButton).toBeTruthy();
    expect(unParticipateButton.textContent).toContain('Do not participate');
  });

  it('should call back() method when back button is clicked', () => {
    const backButton = fixture.debugElement.query(By.css('button[mat-icon-button]'));
    backButton.triggerEventHandler('click', null);
    expect(component.back).toHaveBeenCalled();
  });
});
