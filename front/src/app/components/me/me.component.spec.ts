import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import {of} from "rxjs";
import {SessionInformation} from "../../interfaces/sessionInformation.interface";
import {Router} from "@angular/router";


describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;


  const adminUser = {
    id: 1,
    email: 'c.aguilera@yoga.com',
    lastName: 'Aguilera',
    firstName: 'Christine',
    admin: true,
    password: '123456',
    createdAt: new Date(),
    updatedAt: new Date(),
  }

  const sessionInfo : SessionInformation = {
    admin: false, firstName: "", id: 1, lastName: "", token: "", type: "", username: ""
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(adminUser)),
    delete: jest.fn().mockReturnValue(of(undefined)),
  }

  const mockSessionService = {
    sessionInformation: sessionInfo,
    logOut: jest.fn()
  }

  const mockRouter = {
    navigate: jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService },
                  { provide: UserService, useValue: mockUserService },
                  { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show connected user', () => {
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toBeTruthy();
    let user = component.user;
    if (user) {
      expect(user).toEqual(adminUser);
    }
  });

  it('should delete', () => {
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toBeTruthy();
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
  });



});
