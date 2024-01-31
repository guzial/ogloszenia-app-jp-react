import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Seniority e2e test', () => {
  const seniorityPageUrl = '/seniority';
  const seniorityPageUrlPattern = new RegExp('/seniority(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const senioritySample = {};

  let seniority;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/seniorities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/seniorities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/seniorities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (seniority) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/seniorities/${seniority.id}`,
      }).then(() => {
        seniority = undefined;
      });
    }
  });

  it('Seniorities menu should load Seniorities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('seniority');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Seniority').should('exist');
    cy.url().should('match', seniorityPageUrlPattern);
  });

  describe('Seniority page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(seniorityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Seniority page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/seniority/new$'));
        cy.getEntityCreateUpdateHeading('Seniority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/seniorities',
          body: senioritySample,
        }).then(({ body }) => {
          seniority = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/seniorities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [seniority],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(seniorityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Seniority page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('seniority');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityPageUrlPattern);
      });

      it('edit button click should load edit Seniority page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Seniority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityPageUrlPattern);
      });

      it('edit button click should load edit Seniority page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Seniority');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityPageUrlPattern);
      });

      it('last delete button click should delete instance of Seniority', () => {
        cy.intercept('GET', '/api/seniorities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('seniority').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityPageUrlPattern);

        seniority = undefined;
      });
    });
  });

  describe('new Seniority page', () => {
    beforeEach(() => {
      cy.visit(`${seniorityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Seniority');
    });

    it('should create an instance of Seniority', () => {
      cy.get(`[data-cy="nazwa"]`).type('regarding which industrialization');
      cy.get(`[data-cy="nazwa"]`).should('have.value', 'regarding which industrialization');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        seniority = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', seniorityPageUrlPattern);
    });
  });
});
