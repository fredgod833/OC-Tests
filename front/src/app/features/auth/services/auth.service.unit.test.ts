import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {Observable} from "rxjs";
import {User} from "../../../interfaces/user.interface";
import {AuthService} from "./auth.service";
import {SessionApiService} from "../../sessions/services/session-api.service";
import {Session} from "../../sessions/interfaces/session.interface";
import {SessionInformation} from "../../../interfaces/sessionInformation.interface";
import {LoginRequest} from "../interfaces/loginRequest.interface";
import {RegisterRequest} from "../interfaces/registerRequest.interface";

describe('AuthService', () => {

  let service: AuthService;
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

  const aguileraUser: User = {
    id: 2,
    email: "christina.a@yoga.com",
    lastName: "Aguilera",
    firstName: "Christina",
    admin: false,
    password: "12345",
    createdAt: new Date('2012/05/08'),
    updatedAt: new Date('2023/11/17')
  };

  const loginRequest: LoginRequest = {
    email: "christina.a@yoga.com", password: "12345"
  };

  const loginResponse : SessionInformation = {
    token: "jwt",
    type: "bearer",
    id: 1,
    username: "christina.a@yoga.com",
    firstName: "Christina",
    lastName: "Aguilera",
    admin: false
  };

  const registerRequest: RegisterRequest = {
    email: "christina.a@yoga.com",
    firstName: "Christina",
    lastName: "Aguilera",
    password: "12345"
  };



  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should process log in',() => {

    service.login(loginRequest).subscribe({
      next : (session: SessionInformation) => {
        expect(session).toEqual(loginResponse);
      }
    });

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/auth/login');

    // Assert that the request is a DELETE.
    expect(req.request.method).toEqual('POST');

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush(loginResponse);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  });

  it('should register user',() => {

    service.register(registerRequest)
      .subscribe( next =>
        expect(next).toEqual(expect.anything()));

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/auth/register');

    // Assert that the request is a DELETE.
    expect(req.request.method).toEqual('POST');

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush({});

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });


});
