import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './grupa-tagow.reducer';

export const GrupaTagow = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const grupaTagowList = useAppSelector(state => state.grupaTagow.entities);
  const loading = useAppSelector(state => state.grupaTagow.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="grupa-tagow-heading" data-cy="GrupaTagowHeading">
        <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.home.title">Grupa Tagows</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/grupa-tagow/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.home.createLabel">Create new Grupa Tagow</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {grupaTagowList && grupaTagowList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nazwaGrupy')}>
                  <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.nazwaGrupy">Nazwa Grupy</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nazwaGrupy')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {grupaTagowList.map((grupaTagow, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/grupa-tagow/${grupaTagow.id}`} color="link" size="sm">
                      {grupaTagow.id}
                    </Button>
                  </td>
                  <td>{grupaTagow.nazwaGrupy}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/grupa-tagow/${grupaTagow.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/grupa-tagow/${grupaTagow.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/grupa-tagow/${grupaTagow.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.home.notFound">No Grupa Tagows found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default GrupaTagow;
