describe('Login spec', () => {

  it('Login successfull', () => {
    cy.visit('/login')

    cy.get('input[formControlName="email"]').type("yoga@studio.com")
    cy.get('input[formControlName="password"]').type(`${"test!1234"}`)
    cy.get('button[type="submit"]').click()

    cy.wait(10000)
    cy.url().should('include', '/sessions')
  })

  it('Should display error when wrong password', () => {
    cy.visit('/login')

    cy.get('input[formControlName="email"]').type("yoga@studio.com")
    cy.get('input[formControlName="password"').type(`${"test1234"}{enter}{enter}`)

    cy.get('.error').contains('An error occurred')
  })

  it('Should disable submit button when empty field', () => {
    cy.visit('/login')

    cy.get('input[formControlName="email"]').type("yoga@studio.com")

    cy.get(':button').should('be.disabled')

    cy.get('input[formControlName="password"').type("test1234")

    cy.get(':button').should('be.not.disabled')
  })
});
