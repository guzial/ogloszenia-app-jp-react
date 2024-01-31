import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Wystawca from './wystawca';
import WystawcaDetail from './wystawca-detail';
import WystawcaUpdate from './wystawca-update';
import WystawcaDeleteDialog from './wystawca-delete-dialog';

const WystawcaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Wystawca />} />
    <Route path="new" element={<WystawcaUpdate />} />
    <Route path=":id">
      <Route index element={<WystawcaDetail />} />
      <Route path="edit" element={<WystawcaUpdate />} />
      <Route path="delete" element={<WystawcaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WystawcaRoutes;
