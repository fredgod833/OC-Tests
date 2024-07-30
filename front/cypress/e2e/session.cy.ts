describe('Session spec', () => {

    const mockSession = {
        id: 1,
        name: 'Session 1',
        date: new Date(),
        description: 'Description',
        users: [1, 2, 3],
        createdAt: new Date(),
        updatedAt: new Date(),
        teacher_id: 1,
    };

    const mockSessionWithoutOneUser = {
        id: 1,
        name: 'Session 1',
        date: new Date(),
        description: 'Description',
        users: [2, 3],
        createdAt: new Date(),
        updatedAt: new Date(),
        teacher_id: 1,
    };

    const mockSessionEdited = {
        id: 1,
        name: 'Session with Will',
        date: new Date(),
        description: 'Description',
        users: [1, 2, 3],
        createdAt: new Date(),
        updatedAt: new Date(),
        teacher_id: 1,
    };

    const mockTeacher = {
        id: 1,
        lastName: 'Smith',
        firstName: 'Will',
        createdAt: new Date(),
        updatedAt: new Date()
    }

    it('Create a session successfull', () => {
        cy.login(true, false)

        cy.intercept('GET', '/api/teacher', [mockTeacher]).as('teachers')
        cy.intercept('POST', 'api/session', { mockSession }).as('create session')
      
        cy.get(':button').contains('Create').click()

        cy.url().should('include', '/sessions/create')

        cy.get('input[formControlName=name]').type('Sport')
        cy.get('input[formControlName=date]').type('2024-04-27')
        cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Smith').click()
        cy.get('[formControlName=description]').type('Outside session')

        cy.get(':button').contains('Save').click()

        cy.url().should('include', '/sessions')
        cy.contains('Session created !').should('be.visible');
    })

    it('Display session when admin', () => {
        cy.login(true, true)

        cy.get('mat-card-title').contains(mockSession.name)
        cy.get(':button').contains('Create').should('be.visible')
        cy.get(':button').contains('Edit').should('be.visible')
    })

    it('Display session when no admin', () => {
        cy.login(false, true)

        cy.get('mat-card-title').contains(mockSession.name)
        cy.get(':button').contains('Create').should('not.exist')
        cy.get(':button').contains('Edit').should('not.exist')
    })

    it('Display detail about session when admin', () => {
        cy.login(true, true)

        cy.intercept('GET', '/api/teacher/1', mockTeacher).as('teacher')
        cy.intercept('GET', '/api/session/1', mockSession).as('session')

        cy.get(':button').contains('Detail').click()

        cy.url().should('include', 'sessions/detail/1')

        cy.get('h1').contains(mockSession.name)
        cy.get('.description').contains(mockSession.description)
        cy.get('mat-icon').contains('delete').should('be.visible')

    })

    it('Display detail about session when no admin and unparticipate', () => {
        cy.login(false, true)

        cy.intercept('GET', '/api/teacher/1', mockTeacher).as('teacher')
        cy.intercept('GET', '/api/session/1', mockSession).as('session')
        cy.intercept('DELETE', '/api/session/1/participate/1', {}).as('unparticipate')

        cy.get(':button').contains('Detail').click()

        cy.url().should('include', 'sessions/detail/1')

        cy.get('h1').contains(mockSession.name)
        cy.get('.description').contains(mockSession.description)
        cy.get('mat-icon').contains('delete').should('not.exist')
        cy.get('mat-icon').contains('person_remove').should('be.visible').click()
    })

    it('Display detail about session when no admin and participate', () => {
        cy.login(false, true)

        cy.intercept('GET', '/api/teacher/1', mockTeacher).as('teacher')
        cy.intercept('GET', '/api/session/1', mockSessionWithoutOneUser).as('session')
        cy.intercept('POST', '/api/session/1/participate/1', {}).as('participate')

        cy.get(':button').contains('Detail').click()

        cy.url().should('include', 'sessions/detail/1')

        cy.get('h1').contains(mockSession.name)
        cy.get('.description').contains(mockSession.description)
        cy.get('mat-icon').contains('delete').should('not.exist')
        cy.get('mat-icon').contains('person_add').should('be.visible').click()
    })

    it('Delete session', () => {
        cy.login(true, true)

        cy.intercept('GET', '/api/teacher/1', mockTeacher).as('teacher')
        cy.intercept('GET', '/api/session/1', mockSession).as('session')
        cy.intercept('DELETE', '/api/session/1', {}).as('delete session')

        cy.get(':button').contains('Detail').click()

        cy.url().should('include', 'sessions/detail/1')

        cy.get('h1').contains(mockSession.name)
        cy.get('.description').contains(mockSession.description)
        cy.get('mat-icon').contains('delete').click()
        cy.contains('Session deleted !').should('be.visible');
        cy.url().should('include', 'sessions')
    })

    it('Edit session', () => {
        cy.login(true, true)

        cy.intercept('GET', '/api/teacher/1', mockTeacher).as('teacher')
        cy.intercept('GET', '/api/teacher', [mockTeacher]).as('teachers')
        cy.intercept('GET', '/api/session/1', mockSession).as('session')
        cy.intercept('PUT', '/api/session/1', mockSessionEdited).as('update session')

        cy.get(':button').contains('Edit').click()

        cy.url().should('include', 'sessions/update/1')

        cy.get('h1').contains('Update session')
        cy.get('input[formControlName=name]').clear()
        cy.get('input[formControlName=name]').type("Session with Will")

        cy.get(':button').contains('Save').click()

        cy.contains('Session updated !').should('be.visible')

        cy.url().should('include', 'sessions')
    })
});