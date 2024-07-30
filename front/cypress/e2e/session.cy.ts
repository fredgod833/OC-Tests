import {
  ADMIN_LOGIN, CHRISTINA_LOGIN,
  CHRISTINA_SESSION,
  CHRISTINA_TEACHER,
  SESSION2,
  SESSIONS,
  TEACHER2
} from "./mocked-datas/services.responses";

describe("Session spec", () => {


    it("Create a session successfull", () => {

        cy.intercept("POST","/api/auth/login", {statusCode:200, body:ADMIN_LOGIN }).as("call login service");
        cy.intercept("GET", "/api/teacher", {statusCode:200, body:[CHRISTINA_TEACHER, TEACHER2]}).as("call teacher service");
        cy.intercept("POST", "api/session", {statusCode:200, body: CHRISTINA_SESSION }).as("call session create");
        cy.intercept("GET", "api/session", {statusCode:200, body: SESSIONS }).as("call session service");

        cy.login();
        cy.get(":button").contains("Create").click();
        cy.url().should("include", "/sessions/create");

        cy.get("input[formControlName=name]").type("Yog' Aguilera");
        cy.get("input[formControlName=date]").type("2024-08-05");
        cy.get("mat-select[formControlName=teacher_id]").click().get("mat-option").contains("Christina").click();
        cy.get("[formControlName=description]").type("Yoga with Christina");

        cy.get(":button").contains("Save").click();

        cy.url().should("include", "/sessions");
        cy.contains("Session created !").should("be.visible");
    });

    it("Should hide sessions action for users", () => {

      cy.intercept("POST","/api/auth/login", {statusCode:200, body:CHRISTINA_LOGIN }).as("call login service");
      cy.intercept("GET", "/api/teacher", {statusCode:200, body:[CHRISTINA_TEACHER, TEACHER2]}).as("call teacher service");
      cy.intercept("GET", "/api/teacher/*", {statusCode:200, body:CHRISTINA_TEACHER}).as("call teacher detail");
      cy.intercept("GET", "api/session", {statusCode:200, body: [CHRISTINA_SESSION, SESSION2] }).as("call session service");
      cy.login();

      cy.get("mat-card").contains(CHRISTINA_SESSION.name);
      cy.get(":button").contains("Create").should("not.exist");
      cy.get(":button").contains("Details").should("not.exist");
      cy.get(":button").contains("Edit").should("not.exist");

    });

    it("Should access all actions for admin", () => {

        cy.intercept("POST","/api/auth/login", {statusCode:200, body:ADMIN_LOGIN }).as("call login service");
        cy.intercept("GET", "/api/teacher", {statusCode:200, body:[CHRISTINA_TEACHER, TEACHER2]}).as("call teacher service");
        cy.intercept("GET", "/api/teacher/*", {statusCode:200, body:CHRISTINA_TEACHER}).as("call teacher detail");

        cy.intercept("GET", "api/session", {statusCode:200, body: SESSIONS }).as("call session service");
        cy.intercept("GET", "/api/session/*", CHRISTINA_SESSION).as("call session detail")
        cy.login();

        cy.get(":button" ).contains("Create").should("be.visible");
        cy.get("mat-card").contains(CHRISTINA_SESSION.name);
        let editBtn = cy.get(":button" ).contains("Edit");
        editBtn.should("be.visible");
        let detailBtn = cy.get(":button" ).contains("Detail");
        detailBtn.should("be.visible");
        detailBtn.click();
        cy.url().should("include", "sessions/detail/");
        cy.get("h1").contains(CHRISTINA_SESSION.name)
        cy.get(".description").contains(CHRISTINA_SESSION.description)
        cy.get("mat-icon").contains("delete").should("be.visible")

    });

  it("Should access view actions for users", () => {

    cy.intercept("POST","/api/auth/login", {statusCode:200, body:CHRISTINA_LOGIN }).as("call login service");
    cy.intercept("GET", "/api/teacher", {statusCode:200, body:[CHRISTINA_TEACHER, TEACHER2]}).as("call teacher service");
    cy.intercept("GET", "api/session", {statusCode:200, body: SESSIONS }).as("call session service");
    cy.intercept("GET", "/api/session/*", CHRISTINA_SESSION).as("call session detail")
    cy.intercept("GET", "/api/teacher/*", CHRISTINA_TEACHER).as("call teacher detail")
    cy.login();

    cy.get("mat-card").contains(CHRISTINA_SESSION.name);
    let detailBtn = cy.get(":button" ).contains("Detail");
    detailBtn.should("be.visible");
    detailBtn.click();
    cy.url().should("include", "sessions/detail/");
    cy.get("h1").contains(CHRISTINA_SESSION.name)
    cy.get(".description").contains(CHRISTINA_SESSION.description)
    cy.get("mat-icon").contains("delete").should("not.exist")

  });

/*

      it("Display detail about session when no admin and unparticipate", () => {
          cy.login(false, true)

          cy.intercept("GET", "/api/teacher/1", mockTeacher).as("teacher")
          cy.intercept("GET", "/api/session/1", mockSession).as("session")
          cy.intercept("DELETE", "/api/session/1/participate/1", {}).as("unparticipate")

          cy.get(":button").contains("Detail").click()

          cy.url().should("include", "sessions/detail/1")

          cy.get("h1").contains(mockSession.name)
          cy.get(".description").contains(mockSession.description)
          cy.get("mat-icon").contains("delete").should("not.exist")
          cy.get("mat-icon").contains("person_remove").should("be.visible").click()
      })

      it("Display detail about session when no admin and participate", () => {
          cy.login(false, true)

          cy.intercept("GET", "/api/teacher/1", mockTeacher).as("teacher")
          cy.intercept("GET", "/api/session/1", mockSessionWithoutOneUser).as("session")
          cy.intercept("POST", "/api/session/1/participate/1", {}).as("participate")

          cy.get(":button").contains("Detail").click()

          cy.url().should("include", "sessions/detail/1")

          cy.get("h1").contains(mockSession.name)
          cy.get(".description").contains(mockSession.description)
          cy.get("mat-icon").contains("delete").should("not.exist")
          cy.get("mat-icon").contains("person_add").should("be.visible").click()
      })

      it("Delete session", () => {
          cy.login(true, true)

          cy.intercept("GET", "/api/teacher/1", mockTeacher).as("teacher")
          cy.intercept("GET", "/api/session/1", mockSession).as("session")
          cy.intercept("DELETE", "/api/session/1", {}).as("delete session")

          cy.get(":button").contains("Detail").click()

          cy.url().should("include", "sessions/detail/1")

          cy.get("h1").contains(mockSession.name)
          cy.get(".description").contains(mockSession.description)
          cy.get("mat-icon").contains("delete").click()
          cy.contains("Session deleted !").should("be.visible");
          cy.url().should("include", "sessions")
      })

      it("Edit session", () => {
          cy.login(true, true)

          cy.intercept("GET", "/api/teacher/1", mockTeacher).as("teacher")
          cy.intercept("GET", "/api/teacher", [mockTeacher]).as("teachers")
          cy.intercept("GET", "/api/session/1", mockSession).as("session")
          cy.intercept("PUT", "/api/session/1", mockSessionEdited).as("update session")

          cy.get(":button").contains("Edit").click()

          cy.url().should("include", "sessions/update/1")

          cy.get("h1").contains("Update session")
          cy.get("input[formControlName=name]").clear()
          cy.get("input[formControlName=name]").type("Session with Will")

          cy.get(":button").contains("Save").click()

          cy.contains("Session updated !").should("be.visible")

          cy.url().should("include", "sessions")
      })

   */
});
