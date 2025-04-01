// Import necessary Angular testing modules and utilities
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

import { AuthService } from 'src/app/features/auth/services/auth.service';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;

  // Mock implementation of AuthService with a fake login response
  const authServiceMock = {
    login: jest.fn().mockReturnValue(of({ token: 'fake-token' }))
  };

  beforeEach(async () => {
    // Configure the testing module with component and all dependencies
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ],
      imports: [
        RouterTestingModule,          
        BrowserAnimationsModule,      
        HttpClientModule,             
        MatCardModule,                
        MatIconModule,                
        MatFormFieldModule,           
        MatInputModule,               
        ReactiveFormsModule           
      ]
    }).compileComponents();

    // Create component fixture and instance
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    fixture.detectChanges();
  });

  // Simple test to ensure the component compiles and renders correctly
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Ensure that the submit button is disabled when the form is invalid
  it('should disable submit button when form is invalid', () => {
    const compiled = fixture.nativeElement;
    const submitButton = compiled.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBeTruthy();
  });

  // Simulate a valid form submission and check if AuthService.login is called correctly
  it('should call authService.login when form is valid and submitted', () => {
    component.form.controls['email'].setValue('yoga@studio.com');
    component.form.controls['password'].setValue('test!1234');
    fixture.detectChanges();

    component.submit();

    // Check if AuthService.login was called with the correct payload
    expect(authService.login).toHaveBeenCalledWith({
      email: 'yoga@studio.com',
      password: 'test!1234'
    });
  });
});
