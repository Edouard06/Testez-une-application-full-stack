import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

import { FormComponent } from './form.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

class MockSessionApiService {
  create(session: any) {
    return of({ id: 123, ...session });
  }
  update(id: number, session: any) {
    return of({ id, ...session });
  }
}

describe('FormComponent - Minimal', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: { admin: true }
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useClass: MockSessionApiService }
      ],
      declarations: [FormComponent],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .overrideTemplate(FormComponent, `
      <form [formGroup]="sessionForm" (ngSubmit)="submit()">
        <button type="submit" [disabled]="sessionForm.invalid">Save</button>
      </form>
    `)
    .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    if (!component.sessionForm) {
      component.ngOnInit();
    }
    fixture.detectChanges();
  }));

  it('should create the form component', () => {
    expect(component).toBeTruthy();
  });
});
