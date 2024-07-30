describe('Account spec', () => {

  const adminUser = {
        id: 1,
        email: 'yoga@studio.com',
        lastName: 'Admin',
        firstName: 'Admin',
        admin: true,
        password: 'test!1234',
        createdAt: new Date(),
        updatedAt: new Date()
    }

    const normalUser = {
        id: 3,
        email: 'toto3@toto.com',
        lastName: 'toto',
        firstName: 'toto',
        admin: false,
        password: 'test!1234',
        createdAt: new Date(),
        updatedAt: new Date()
    }

    it('Test admin successfull', () => {
        //cy.login(true)
        cy.visit('/login')
        cy.get('input[formControlName=email').type("yoga@studio.com")
        cy.get('input[formControlName=password').type(`${"test!1234"}{enter}{enter}`)
        cy.url().should('include', '/sessions')

        cy.intercept('GET', '/api/user/1', adminUser).as('user')

        cy.get('.link').contains('Account').click()

        cy.url().should('include', '/me')

        cy.get('p').contains('yoga@studio.com')
        cy.get('.my2').contains('You are admin')
    })

    it('Test no admin successfull', () => {
        //cy.login(false)
        cy.visit('/login')
        cy.get('input[formControlName=email').type("toto3@toto.com")
        cy.get('input[formControlName=password').type(`${"test!1234"}{enter}{enter}`)
        cy.url().should('include', '/sessions')

        cy.intercept('GET', '/api/user/3', normalUser).as('user')

        cy.get('.link').contains('Account').click()

        cy.url().should('include', '/me')

        cy.get('p').contains('toto3@toto.com')
        cy.get(':button').contains('Detail')
    })
  });
