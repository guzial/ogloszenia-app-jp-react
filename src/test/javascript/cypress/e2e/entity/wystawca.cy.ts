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

describe('Wystawca e2e test', () => {
  const wystawcaPageUrl = '/wystawca';
  const wystawcaPageUrlPattern = new RegExp('/wystawca(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const wystawcaSample = {};

  let wystawca;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/wystawcas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/wystawcas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/wystawcas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (wystawca) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/wystawcas/${wystawca.id}`,
      }).then(() => {
        wystawca = undefined;
      });
    }
  });

  it('Wystawcas menu should load Wystawcas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('wystawca');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Wystawca').should('exist');
    cy.url().should('match', wystawcaPageUrlPattern);
  });

  describe('Wystawca page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(wystawcaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Wystawca page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/wystawca/new$'));
        cy.getEntityCreateUpdateHeading('Wystawca');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', wystawcaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/wystawcas',
          body: wystawcaSample,
        }).then(({ body }) => {
          wystawca = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/wystawcas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [wystawca],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(wystawcaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Wystawca page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('wystawca');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', wystawcaPageUrlPattern);
      });

      it('edit button click should load edit Wystawca page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Wystawca');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', wystawcaPageUrlPattern);
      });

      it('edit button click should load edit Wystawca page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Wystawca');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', wystawcaPageUrlPattern);
      });

      it('last delete button click should delete instance of Wystawca', () => {
        cy.intercept('GET', '/api/wystawcas/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('wystawca').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', wystawcaPageUrlPattern);

        wystawca = undefined;
      });
    });
  });

  describe('new Wystawca page', () => {
    beforeEach(() => {
      cy.visit(`${wystawcaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Wystawca');
    });

    it('should create an instance of Wystawca', () => {
      cy.get(`[data-cy="nazwa"]`).type('abaft drat');
      cy.get(`[data-cy="nazwa"]`).should('have.value', 'abaft drat');

      cy.get(`[data-cy="kontakt"]`).type('if recycle');
      cy.get(`[data-cy="kontakt"]`).should('have.value', 'if recycle');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        wystawca = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', wystawcaPageUrlPattern);
    });
  });
});
