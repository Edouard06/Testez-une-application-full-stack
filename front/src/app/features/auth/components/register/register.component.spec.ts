import { HttpClientModule } from '@angular/common/http';
import { Component, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

// sMOCK Angular Material Components
@Component({ selector: 'mat-card', template: '<ng-content></ng-content>' })
class MockMatCard {}

@Component({ selector: 'mat-card-header', template: '<ng-content></ng-content>' })
class MockMatCardHeader {}

@Component({ selector: 'mat-card-title', template: '<ng-content></ng-content>' })
class MockMatCardTitle {}

@Component({ selector: 'mat-card-content', template: '<ng-content></ng-content>' })
class MockMatCardContent {}

@Component({ selector: 'mat-form-field', template: '<ng-content></ng-content>' })
class MockMatFormField {}

@Component({ selector: 'mat-icon', template: '<ng-content></ng-content>' })
class MockMatIcon {}

@Component({ selector: 'input[matInput]', template: '' })
class MockMatInput {}

@Component({ selector: 'button[mat-raised-button]', template: '<ng-content></ng-content>' })
class MockMatRaisedButton {}

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;

  const mockAuthService = {
    register: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        RegisterComponent,
        MockMatCard,
        MockMatCardHeader,
        MockMatCardTitle,
        MockMatCardContent,
        MockMatFormField,
        MockMatIcon,
        MockMatInput,
        MockMatRaisedButton
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not register and show error when form is invalid', () => {
    jest.spyOn(mockAuthService, 'register').mockImplementation(() => throwError(() => new Error('')));
    component.form.setValue({ email: 'qsd@qsd.fr', firstName: '', lastName: 'QSD', password: 'pass' });
    component.submit();
    expect(mockAuthService.register).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });

  it('should not register and show error with invalid email', () => {
    jest.spyOn(mockAuthService, 'register').mockImplementation(() => throwError(() => new Error('')));
    component.form.setValue({ email: 'qsd@qsd', firstName: 'qsd', lastName: 'QSD', password: 'pass' });
    component.submit();
    expect(mockAuthService.register).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });

  it('should register and navigate on valid form submission', () => {
    jest.spyOn(mockAuthService, 'register').mockImplementation(() => of({}));
    const routerSpy = jest.spyOn(router, 'navigate');
    component.form.setValue({ email: 'qsd@qsd.fr', firstName: 'qsd', lastName: 'QSD', password: 'pass' });
    component.submit();
    expect(mockAuthService.register).toHaveBeenCalled();
    expect(component.onError).toBe(false);
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
  });
});
