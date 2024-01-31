import ogloszenie from 'app/entities/ogloszenie/ogloszenie.reducer';
import wystawca from 'app/entities/wystawca/wystawca.reducer';
import tag from 'app/entities/tag/tag.reducer';
import grupaTagow from 'app/entities/grupa-tagow/grupa-tagow.reducer';
import seniority from 'app/entities/seniority/seniority.reducer';
import typUmowy from 'app/entities/typ-umowy/typ-umowy.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  ogloszenie,
  wystawca,
  tag,
  grupaTagow,
  seniority,
  typUmowy,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
