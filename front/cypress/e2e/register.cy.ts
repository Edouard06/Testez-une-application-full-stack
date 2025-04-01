describe('RegisterTestSuites', () => {
    it('Register successfull', () => {
        cy.visit('/register')
    
        cy.intercept('POST', '/api/auth/register', {
            body: {
              firstName: 'Iso',
              lastName: 'Yoda',
              email: 'yoda@gmail.com',
              password: 'azerty'
            },
          })
    
          cy.get('input[formControlName=email]').type("mario@gmail.com")
          cy.get('input[formControlName=firstName]').type("Mario")
          cy.get('input[formControlName=lastName]').type("Mario")
          cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
      
          cy.url().should('include', '/login')
      
    });
    
    it('Register unsuccessfull', () => {
      cy.visit('/register')
    
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 404,
        body: 'Not Found',
      }).as('apiRequest');
    
        cy.get('input[formControlName=email]').type("mario@gmail.com")
        cy.get('input[formControlName=firstName]').type("Mario")
        cy.get('input[formControlName=lastName]').type("Mario")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
        cy.get('.error').should('be.visible');
    
    });
    
    });