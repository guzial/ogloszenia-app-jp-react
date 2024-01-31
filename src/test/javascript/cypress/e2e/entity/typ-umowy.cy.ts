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

describe('TypUmowy e2e test', () => {
  const typUmowyPageUrl = '/typ-umowy';
  const typUmowyPageUrlPattern = new RegExp('/typ-umowy(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typUmowySample = {};

  let typUmowy;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/typ-umowies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/typ-umowies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/typ-umowies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typUmowy) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/typ-umowies/${typUmowy.id}`,
      }).then(() => {
        typUmowy = undefined;
      });
    }
  });

  it('TypUmowies menu should load TypUmowies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('typ-umowy');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypUmowy').should('exist');
    cy.url().should('match', typUmowyPageUrlPattern);
  });

  describe('TypUmowy page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typUmowyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypUmowy page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/typ-umowy/new$'));
        cy.getEntityCreateUpdateHeading('TypUmowy');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typUmowyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/typ-umowies',
          body: typUmowySample,
        }).then(({ body }) => {
          typUmowy = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/typ-umowies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [typUmowy],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typUmowyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypUmowy page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typUmowy');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typUmowyPageUrlPattern);
      });

      it('edit button click should load edit TypUmowy page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypUmowy');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typUmowyPageUrlPattern);
      });

      it('edit button click should load edit TypUmowy page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypUmowy');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typUmowyPageUrlPattern);
      });

      it('last delete button click should delete instance of TypUmowy', () => {
        cy.intercept('GET', '/api/typ-umowies/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('typUmowy').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typUmowyPageUrlPattern);

        typUmowy = undefined;
      });
    });
  });

  describe('new TypUmowy page', () => {
    beforeEach(() => {
      cy.visit(`${typUmowyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypUmowy');
    });

    it('should create an instance of TypUmowy', () => {
      cy.get(`[data-cy="tekst"]`).type('use');
      cy.get(`[data-cy="tekst"]`).should('have.value', 'use');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typUmowy = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typUmowyPageUrlPattern);
    });
  });
});
