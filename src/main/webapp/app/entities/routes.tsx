import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ogloszenie from './ogloszenie';
import Wystawca from './wystawca';
import Tag from './tag';
import GrupaTagow from './grupa-tagow';
import Seniority from './seniority';
import TypUmowy from './typ-umowy';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="ogloszenie/*" element={<Ogloszenie />} />
        <Route path="wystawca/*" element={<Wystawca />} />
        <Route path="tag/*" element={<Tag />} />
        <Route path="grupa-tagow/*" element={<GrupaTagow />} />
        <Route path="seniority/*" element={<Seniority />} />
        <Route path="typ-umowy/*" element={<TypUmowy />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
