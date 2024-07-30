import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import {SessionService} from "./session.service";
import {User} from "../interfaces/user.interface";
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {

  let service: SessionService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;


  const cordulaUser: User = {
    id: 1,
    email: "cristina.c@yoga.com",
    lastName: "Cordula",
    firstName: "Cristina",
    admin: false,
    password: "12345",
    createdAt: new Date('2012/05/08'),
    updatedAt: new Date('2023/11/17')
  };

  const sessionInfo : SessionInformation = {
    admin: false, firstName: "Cristina", id: 1, lastName: "Cordula", token: "jwt", type: "admin", username: "cristina.c@yoga.com"
  };


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should connect user', () => {
    service.logIn(sessionInfo);

    expect(service.isLogged).toBeTruthy();
    expect(service.sessionInformation).toEqual(sessionInfo);
  });

  it('should disconnect user', () => {
    service.logOut();

    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();
  });

});
