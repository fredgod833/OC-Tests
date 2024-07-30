import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { UserService } from './user.service';
import {User} from "../interfaces/user.interface";


describe('UserService', () => {

  let service: UserService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  const normalUser : User = {
    id: 1,
    email: "cristina.c@yoga.com",
    lastName: "Cordula",
    firstName: "Cristina",
    admin : false,
    password : "12345",
    createdAt: new Date('2012/05/08'),
    updatedAt: new Date('2023/11/17')
  };

  const adminUser : User = {
    id: 1,
    email: "admin@yoga.com",
    lastName: "Administrator",
    firstName: "The",
    admin : true,
    password : "nimda",
    createdAt: new Date('2011/12/23'),
    updatedAt: new Date('2024/01/01')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return expected user', () => {

    // Expect to have normalUser out of the observable
    service.getById('1')
      .subscribe( (user : User) =>
        expect(user).toEqual(normalUser));

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/user/1');

    // Assert that the request is a GET.
    expect(req.request.method).toEqual('GET');

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush(normalUser);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  })

  it('should delete user', () => {

    // Expect to have normalUser out of the observable
    service.delete('1')
      .subscribe( next =>
        expect(next).toEqual(expect.anything()));

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/user/1');

    // Assert that the request is a DELETE.
    expect(req.request.method).toEqual('DELETE');

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush({});

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  })

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

});
