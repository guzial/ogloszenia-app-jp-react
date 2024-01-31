import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ogloszenie from './ogloszenie';
import OgloszenieDetail from './ogloszenie-detail';
import OgloszenieUpdate from './ogloszenie-update';
import OgloszenieDeleteDialog from './ogloszenie-delete-dialog';

const OgloszenieRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ogloszenie />} />
    <Route path="new" element={<OgloszenieUpdate />} />
    <Route path=":id">
      <Route index element={<OgloszenieDetail />} />
      <Route path="edit" element={<OgloszenieUpdate />} />
      <Route path="delete" element={<OgloszenieDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OgloszenieRoutes;
