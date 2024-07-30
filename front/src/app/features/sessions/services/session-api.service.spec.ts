import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';
import { SessionApiService } from './session-api.service';
import {Observable} from "rxjs";

describe('SessionsService', () => {

  let service: SessionApiService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  const newSession : Session = {
    name: "YOGAguilera Session I",
    description: "Session animée par Christina Aguilera",
    date: new Date('2024/06/21'),
    teacher_id: 2,
    users: [1]
  };

  const session1 : Session = {
    id: 1,
    name: "YOGAguilera Session I",
    description: "Session animée par Christina Aguilera",
    date: new Date('2024/06/21'),
    teacher_id: 2,
    users: [1],
    createdAt: new Date('2011/12/23'),
    updatedAt: new Date('2011/12/23')
  };

  const session2 : Session = {
    id: 1,
    name: "YOGAguilera Session II",
    description: "Session animée par Christina Aguilera",
    date: new Date('2024/06/25'),
    teacher_id: 2,
    users: [1],
    createdAt: new Date('2011/12/23'),
    updatedAt: new Date('2011/12/23')
  };


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  //https://www.udemy.com/share/101ZkE3@mAHWAiFznqypmilmxa1_GuJgarESMV0TmV8LFhjRGbiK1dRaCC2XZ90J_HFmLCXlfA==/
  //https://codecraft.tv/courses/angular/unit-testing/mocks-and-spies/


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list sessions', done => {

       service.all().subscribe({
         next : (session: Session[]) => {
            expect(session).toHaveLength(2);
            expect(session[0]).toEqual(session1);
            expect(session[1]).toEqual(session2);
            done();
         }
       });

      // The following `expectOne()` will match the request's URL.
      const req : TestRequest = httpTestingController.expectOne('api/session');

      // Assert that the request is a DELETE.
      expect(req.request.method).toEqual('GET');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      req.flush([session1, session2]);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
  })

  it('should get session 1', () => {
    let session : Observable<Session> = service.detail("1");
    expect(session).toBeTruthy()
  })

  it('should return expected session', () => {

    // Expect to have normalUser out of the observable
    service.detail('1')
      .subscribe( (session : Session) =>
        expect(session).toEqual(session1));

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/session/1');

    // Assert that the request is a GET.
    expect(req.request.method).toEqual('GET');

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush(session1);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  })

  it('should delete one session', () => {

    // Expect to have normalUser out of the observable
    service.delete('1')
      .subscribe( next =>
        expect(next).toEqual(expect.anything()));

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/session/1');

    // Assert that the request is a DELETE.
    expect(req.request.method).toEqual('DELETE');

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush({})

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  })

  it('should create one session', () => {

    // Expect to have normalUser out of the observable
    service.create(newSession)
      .subscribe( next =>
        expect(next).toEqual(session1));

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/session');

    // Assert that the request is a DELETE.
    expect(req.request.method).toEqual('POST');

    // Assert that the request is a DELETE.
    expect(req.request.body).toHaveProperty("name","YOGAguilera Session I");

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush(session1);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  })

  it('should update the session', () => {

    // Expect to have normalUser out of the observable
    service.update("1", session2)
      .subscribe( next =>
        expect(next).toEqual(session2));

    // The following `expectOne()` will match the request's URL.
    const req : TestRequest = httpTestingController.expectOne('api/session/1');

    // Assert that the request is a DELETE.
    expect(req.request.method).toEqual('PUT');

    // Assert that the request is a DELETE.
    expect(req.request.body).toHaveProperty("name","YOGAguilera Session II");

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush(session2);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();

  })

});
