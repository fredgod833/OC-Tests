describe('Register spec', () => {

    it('should Register', () => {

        cy.intercept('POST', '/api/auth/register', {
          statusCode: 200,
          body:
            {
              "message": "User registered successfully!"
            }
        });

        cy.visit('/register');
        cy.get('input[formControlName=firstName').type("Admin");
        cy.get('input[formControlName=lastName').type("Admin");
        cy.get('input[formControlName="email"]').type("yoga@studio.com");
        cy.get('input[formControlName="password"]').type("test!1234");
        cy.get('button[type="submit"]').click();
        cy.url().should('include', '/login');

    });

    it('Should disable button on incomplete form', () => {
        cy.visit('/register');
        cy.get('input[formControlName="email"]').type("yoga@studio.com");
        cy.get('input[formControlName="password"]').type("test!1234");
        cy.get('button[type="submit"]').should('be.disabled');
        cy.get('input[formControlName=firstName').type("Admin");
        cy.get('input[formControlName=lastName').type("Admin");
        cy.get('button[type="submit"]').should('be.not.disabled');
    });

  });
