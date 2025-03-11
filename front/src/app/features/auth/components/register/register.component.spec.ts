import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the register component', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form with 4 controls (firstName, lastName, email, password)', () => {
    // Vérifie que le formulaire contient bien les contrôles attendus.
    expect(component.form.contains('firstName')).toBeTruthy();
    expect(component.form.contains('lastName')).toBeTruthy();
    expect(component.form.contains('email')).toBeTruthy();
    expect(component.form.contains('password')).toBeTruthy();
  });

  it('should mark the firstName control as invalid if empty', () => {
    const control = component.form.get('firstName');
    control?.setValue('');
    expect(control?.valid).toBeFalsy();
  });

  it('should disable submit button if form is invalid', () => {
    // Le formulaire est initialement vide et invalide.
    fixture.detectChanges();
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    expect(submitButton.disabled).toBeTruthy();
  });

  it('should enable submit button when form is valid', () => {
    // Remplit tous les contrôles avec des valeurs valides.
    component.form.get('firstName')?.setValue('John');
    component.form.get('lastName')?.setValue('Doe');
    component.form.get('email')?.setValue('john.doe@example.com');
    component.form.get('password')?.setValue('Password123!');
    fixture.detectChanges();

    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    expect(component.form.valid).toBeTruthy();
    expect(submitButton.disabled).toBeFalsy();
  });

  it('should display error message when onError is true', () => {
    // Active la propriété onError et vérifie que le message d'erreur s'affiche.
    component.onError = true;
    fixture.detectChanges();
    const errorElem = fixture.debugElement.query(By.css('.error'));
    expect(errorElem).toBeTruthy();
    expect(errorElem.nativeElement.textContent).toContain('An error occurred');
  });

  it('should call submit() method when form is submitted', () => {
    // Espionne la méthode submit pour vérifier qu'elle est appelée lors de la soumission.
    jest.spyOn(component, 'submit');
    // Remplit le formulaire avec des valeurs valides.
    component.form.get('firstName')?.setValue('Jane');
    component.form.get('lastName')?.setValue('Doe');
    component.form.get('email')?.setValue('jane.doe@example.com');
    component.form.get('password')?.setValue('Password123!');
    fixture.detectChanges();

    // Simule la soumission du formulaire.
    const formElem = fixture.debugElement.query(By.css('form'));
    formElem.triggerEventHandler('ngSubmit', null);
    expect(component.submit).toHaveBeenCalled();
  });
});
