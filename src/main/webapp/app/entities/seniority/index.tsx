import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Seniority from './seniority';
import SeniorityDetail from './seniority-detail';
import SeniorityUpdate from './seniority-update';
import SeniorityDeleteDialog from './seniority-delete-dialog';

const SeniorityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Seniority />} />
    <Route path="new" element={<SeniorityUpdate />} />
    <Route path=":id">
      <Route index element={<SeniorityDetail />} />
      <Route path="edit" element={<SeniorityUpdate />} />
      <Route path="delete" element={<SeniorityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SeniorityRoutes;
