// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
declare namespace Cypress {
    interface Chainable {
      login(admin: boolean): typeof login
    }
}

function login(admin: boolean): void {
    cy.visit('/login')

    if (admin) {
      cy.get('input[formControlName="email"]').type("yoga@studio.com")
      cy.get('input[formControlName="password"]').type(`${"test!1234"}{enter}{enter}`)
    } else {
      cy.get('input[formControlName="email"]').type("toto3@toto.com")
      cy.get('input[formControlName="password"]').type(`${"test!1234"}{enter}{enter}`)
    }

    cy.url().should('include', '/sessions')
}

Cypress.Commands.add('login', login)
