describe('Register spec', () => {
    it('should Register successfull', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {}).as('register')

        cy.get('input[formControlName=firstName]').type("toto")
        cy.get('input[formControlName=lastName]').type("toto")
        cy.get('input[formControlName=email]').type("toto5@oc.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}`)

        cy.url().should('include', '/login')
    })

    it('Should disable submit button on empty field', () => {
        cy.visit('/register')

        cy.get('input[formControlName=email').type("yoga@studio.com")
        cy.get('input[formControlName=password').type("test!1234")

        cy.get(':button').should('be.disabled')

        cy.get('input[formControlName=firstName').type("Admin")
        cy.get('input[formControlName=lastName').type("Admin")

        cy.get(':button').should('be.not.disabled')
    })
  });
