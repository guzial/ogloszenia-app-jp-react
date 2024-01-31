import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TypUmowy from './typ-umowy';
import TypUmowyDetail from './typ-umowy-detail';
import TypUmowyUpdate from './typ-umowy-update';
import TypUmowyDeleteDialog from './typ-umowy-delete-dialog';

const TypUmowyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TypUmowy />} />
    <Route path="new" element={<TypUmowyUpdate />} />
    <Route path=":id">
      <Route index element={<TypUmowyDetail />} />
      <Route path="edit" element={<TypUmowyUpdate />} />
      <Route path="delete" element={<TypUmowyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TypUmowyRoutes;
