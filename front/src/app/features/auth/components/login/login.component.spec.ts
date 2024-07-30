import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {SessionInformation} from 'src/app/interfaces/sessionInformation.interface';

import {LoginComponent} from './login.component';
import {AuthService} from "../../services/auth.service";
import {of, throwError} from "rxjs";
import {ListComponent} from "../../../sessions/components/list/list.component";
import {NgZone} from "@angular/core";
import {Router} from "@angular/router";

describe('LoginComponent', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const sessionInfo : SessionInformation = {
    admin: false, firstName: "", id: 0, lastName: "", token: "", type: "", username: ""
  };

  const mockAuthService = {
    login: jest.fn().mockReturnValue(of(sessionInfo))
  }

  const mockSessionService = {
    logIn: jest.fn()
  }

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [{ provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService }
      ],
      imports: [
        RouterTestingModule.withRoutes([
              { path: '*', component: ListComponent },
              { path: 'sessions', component: ListComponent }
        ]),
        BrowserAnimationsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should log user in', () => {
    component.submit();
    expect(mockAuthService.login).toHaveBeenCalled();
    expect(mockSessionService.logIn).toHaveBeenCalled();
  });

  it('should display connection errors', () => {
    jest.spyOn(mockAuthService, 'login').mockReturnValue(throwError(() => new Error('Connection failure')));
    component.submit();
    expect(component.onError).toBe(true);
  })

});
