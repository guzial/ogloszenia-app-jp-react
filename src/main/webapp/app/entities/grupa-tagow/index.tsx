import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GrupaTagow from './grupa-tagow';
import GrupaTagowDetail from './grupa-tagow-detail';
import GrupaTagowUpdate from './grupa-tagow-update';
import GrupaTagowDeleteDialog from './grupa-tagow-delete-dialog';

const GrupaTagowRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GrupaTagow />} />
    <Route path="new" element={<GrupaTagowUpdate />} />
    <Route path=":id">
      <Route index element={<GrupaTagowDetail />} />
      <Route path="edit" element={<GrupaTagowUpdate />} />
      <Route path="delete" element={<GrupaTagowDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GrupaTagowRoutes;
