import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { expect, jest } from '@jest/globals';

describe('MeComponent (Jest)', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockUserService: any;
  let mockSnackBar: any;
  let mockRouter: any;
  let mockSessionService: any;

  const fakeUser = {
    id: 1,
    firstName: 'Yoda',
    lastName: 'Test',
    email: 'yoda@test.com',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    mockUserService = {
      getById: jest.fn().mockReturnValue(of(fakeUser)),
      delete: jest.fn().mockReturnValue(of({}))
    };

    mockSnackBar = {
      open: jest.fn()
    };

    mockRouter = {
      navigate: jest.fn()
    };

    mockSessionService = {
      sessionInformation: { id: 1 },
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user on init', () => {
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(fakeUser);
  });

  it('should call history.back when back() is triggered', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete user and redirect', fakeAsync(() => {
    component.delete();
    tick();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  }));
});
