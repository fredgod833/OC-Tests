import {ADMIN_LOGIN, CHRISTINA_LOGIN, LOGIN_INVALID, SESSIONS} from "./mocked-datas/services.responses";

describe('Login spec', () => {

  beforeEach(() => {
    //Mock Service Session
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: SESSIONS
    }).as("call sessions service");
  });

  it('Should disable submit button on empty field', () => {
    cy.visit('/login')
    cy.get('input[formControlName="email"]').type("yoga@studio.com")
    cy.get(':button').should('be.disabled')
    cy.get('input[formControlName="password"').type("test!1234")
    cy.get(':button').should('be.not.disabled')

  });

  it('Should display error when wrong password', () => {
    //Mock Service Log in
    cy.intercept('POST', 'api/auth/login', {statusCode:401, body:LOGIN_INVALID }).as("call login service");
    cy.visit('/login')
    cy.get('input[formControlName="email"]').type("toto@titi.com")
    cy.get('input[formControlName="password"]').type("toto!2024")
    cy.get('button[type="submit"]').click();
    cy.get('.error').contains('An error occurred');

  });

  it('Should log in', () => {
    //Mock Service Log in
    cy.intercept("POST","/api/auth/login", {statusCode:200, body:ADMIN_LOGIN }).as("call login service");
    cy.visit('/login')
    cy.get('input[formControlName="email"]').type("yoga@studio.com")
    cy.get('input[formControlName="password"]').type("test!1234")
    cy.get('button[type="submit"]').click()
    cy.url().should('include', '/sessions')
  });

  it('Should log out', () => {

    //Mock login and session service
    cy.intercept("POST","/api/auth/login", {statusCode:200, body:CHRISTINA_LOGIN}).as("call login service");
    cy.intercept('GET', '/api/session', {body: SESSIONS, statusCode: 200}).as("call sessions service");
    // Log in
    cy.login();

    // Click Logout button
    cy.get('.link').contains('Logout').click()
    cy.url().should('include', '')

  })

});
