import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { expect, jest } from '@jest/globals';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

describe('DetailComponent (Jest)', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSession = {
    teacher_id: 10,
    name: 'yoga session',
    description: 'Relaxing yoga session',
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
    users: [1, 2]
  };

  const mockTeacher = {
    id: 10,
    firstName: 'Alice',
    lastName: 'Smith',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        ReactiveFormsModule 
      ],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: new Map([['id', '123']]) } } },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] 
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session and teacher on init', fakeAsync(() => {
    component.ngOnInit();
    tick();

    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
    expect(mockTeacherService.detail).toHaveBeenCalledWith('10');
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
    expect(component.isParticipate).toBe(true);
  }));

  it('should call back() and trigger window.history.back', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete session and redirect', fakeAsync(() => {
    component.delete();
    tick();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith('123');
    expect(mockSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('should call participate and refresh session', fakeAsync(() => {
    const fetchSpy = jest.spyOn<any, any>(component as any, 'fetchSession');
    component.participate();
    tick();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('123', '1');
    expect(fetchSpy).toHaveBeenCalled();
  }));

  it('should call unParticipate and refresh session', fakeAsync(() => {
    const fetchSpy = jest.spyOn<any, any>(component as any, 'fetchSession');
    component.unParticipate();
    tick();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('123', '1');
    expect(fetchSpy).toHaveBeenCalled();
  }));
});
