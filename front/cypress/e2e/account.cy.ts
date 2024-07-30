import {SESSIONS, ADMIN_USER, ADMIN_LOGIN, CHRISTINA_LOGIN, CHRISTINA_USER} from "./mocked-datas/services.responses";

describe('Account tests', () => {

  beforeEach(() => {
    //Mock session service
    cy.intercept('GET', '/api/session', { statusCode:200, body:SESSIONS }).as("call sessions service");
  });

  it('Should show is admin', () => {
    //Mock login and user service
    cy.intercept("POST","/api/auth/login", {statusCode:200, body:ADMIN_LOGIN}).as("call login service");
    cy.intercept("GET","/api/user/*", {statusCode:200, body:ADMIN_USER}).as("call user service");

    // login process
    cy.login();

    cy.get('.link').contains('Account').click();
    cy.url().should('include', '/me');
    //vérifier les infos
    cy.get('p').contains('Admin ADMIN');
    cy.get('p').contains('yoga@studio.com');
    cy.get('p').contains('You are admin');

  });

  it('Should not show is admin', () => {

    cy.intercept("POST","/api/auth/login", {statusCode:200, body:CHRISTINA_LOGIN}).as("call login service");
    cy.intercept("GET","/api/user/*", {statusCode:200, body:CHRISTINA_USER}).as("call user service");
    // login process
    cy.login();

    cy.get('.link').contains('Account').click();
    cy.url().should('include', '/me');
    cy.get('p').contains('Christina AGUILERA');
    cy.get('p').contains('caguilera@studio.com');
    cy.get(':button').contains('Detail');

  });

});
