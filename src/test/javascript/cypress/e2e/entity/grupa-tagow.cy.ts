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

describe('GrupaTagow e2e test', () => {
  const grupaTagowPageUrl = '/grupa-tagow';
  const grupaTagowPageUrlPattern = new RegExp('/grupa-tagow(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const grupaTagowSample = {};

  let grupaTagow;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/grupa-tagows+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/grupa-tagows').as('postEntityRequest');
    cy.intercept('DELETE', '/api/grupa-tagows/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (grupaTagow) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/grupa-tagows/${grupaTagow.id}`,
      }).then(() => {
        grupaTagow = undefined;
      });
    }
  });

  it('GrupaTagows menu should load GrupaTagows page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('grupa-tagow');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GrupaTagow').should('exist');
    cy.url().should('match', grupaTagowPageUrlPattern);
  });

  describe('GrupaTagow page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(grupaTagowPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GrupaTagow page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/grupa-tagow/new$'));
        cy.getEntityCreateUpdateHeading('GrupaTagow');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', grupaTagowPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/grupa-tagows',
          body: grupaTagowSample,
        }).then(({ body }) => {
          grupaTagow = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/grupa-tagows+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [grupaTagow],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(grupaTagowPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GrupaTagow page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('grupaTagow');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', grupaTagowPageUrlPattern);
      });

      it('edit button click should load edit GrupaTagow page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GrupaTagow');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', grupaTagowPageUrlPattern);
      });

      it('edit button click should load edit GrupaTagow page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GrupaTagow');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', grupaTagowPageUrlPattern);
      });

      it('last delete button click should delete instance of GrupaTagow', () => {
        cy.intercept('GET', '/api/grupa-tagows/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('grupaTagow').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', grupaTagowPageUrlPattern);

        grupaTagow = undefined;
      });
    });
  });

  describe('new GrupaTagow page', () => {
    beforeEach(() => {
      cy.visit(`${grupaTagowPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GrupaTagow');
    });

    it('should create an instance of GrupaTagow', () => {
      cy.get(`[data-cy="nazwaGrupy"]`).type('progenitor');
      cy.get(`[data-cy="nazwaGrupy"]`).should('have.value', 'progenitor');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        grupaTagow = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', grupaTagowPageUrlPattern);
    });
  });
});
