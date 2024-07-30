import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import {By} from "@angular/platform-browser";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {Session} from "../../interfaces/session.interface";
import {of} from "rxjs";
import {SessionApiService} from "../../services/session-api.service";
import {TeacherService} from "../../../../services/teacher.service";

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSession: Session = {
    name: 'Cordula Session',
    date: new Date(),
    description: 'Description',
    users: Array(10).fill({}),
    createdAt: new Date(),
    updatedAt: new Date(),
    teacher_id: 1,
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockImplementation(() => of(undefined)),
    unParticipate: jest.fn().mockImplementation(() => of(undefined))
  };

  const mockTeacher = {
    id: 1,
    lastName: 'Cordula',
    firstName: 'Cristina',
    createdAt: new Date(),
    updatedAt: new Date()
  }

  const mockTeacherService = {
    detail: jest.fn().mockImplementation(() => of(mockTeacher))
  }
/*
  const mockRouter = {
    navigate: jest.fn()
  }
*/
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService }//,
       // { provide: Router, useValue: mockRouter }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show delete button for admin user', () => {
    expect(
      fixture.debugElement
        .queryAll(By.css('button'))
        .find((button) => button.nativeElement.textContent.includes('delete'))
    ).toBeTruthy();
  })

  it('shouldn\'t show delete button when user is not admin', () => {
    component.isAdmin = false
    fixture.detectChanges();
    expect(
      fixture.debugElement
        .queryAll(By.css('button'))
        .find((button) => button.nativeElement.textContent.includes('delete'))
    ).toBeFalsy();
  })

  it('should show session information', () => {
    const sessionNameElement = fixture.debugElement.query(By.css('h1')).nativeElement;
    expect(sessionNameElement.textContent).toContain('Cordula Session');

    const sessionDescriptionElement = fixture.debugElement.query(By.css('.description')).nativeElement;
    expect(sessionDescriptionElement.textContent).toContain('Description');
  })

  it('should register participation', () => {
    component.sessionId = '1';
    component.userId = '5';
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '5');
    expect(component.session).toEqual(mockSession);
  })

});

