import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './ogloszenie.reducer';

export const Ogloszenie = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const ogloszenieList = useAppSelector(state => state.ogloszenie.entities);
  const loading = useAppSelector(state => state.ogloszenie.loading);
  const links = useAppSelector(state => state.ogloszenie.links);
  const updateSuccess = useAppSelector(state => state.ogloszenie.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="ogloszenie-heading" data-cy="OgloszenieHeading">
        <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.home.title">Ogloszenies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ogloszenie/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.home.createLabel">Create new Ogloszenie</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={ogloszenieList ? ogloszenieList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {ogloszenieList && ogloszenieList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('tytul')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.tytul">Tytul</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('tytul')} />
                  </th>
                  <th className="hand" onClick={sort('opis')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.opis">Opis</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('opis')} />
                  </th>
                  <th className="hand" onClick={sort('dataPublikacji')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.dataPublikacji">Data Publikacji</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('dataPublikacji')} />
                  </th>
                  <th className="hand" onClick={sort('dataWaznosci')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.dataWaznosci">Data Waznosci</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('dataWaznosci')} />
                  </th>
                  <th className="hand" onClick={sort('startOd')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.startOd">Start Od</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('startOd')} />
                  </th>
                  <th className="hand" onClick={sort('czyWidelki')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.czyWidelki">Czy Widelki</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('czyWidelki')} />
                  </th>
                  <th className="hand" onClick={sort('widelkiMin')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.widelkiMin">Widelki Min</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('widelkiMin')} />
                  </th>
                  <th className="hand" onClick={sort('widelkiMax')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.widelkiMax">Widelki Max</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('widelkiMax')} />
                  </th>
                  <th className="hand" onClick={sort('aktywne')}>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.aktywne">Aktywne</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('aktywne')} />
                  </th>
                  <th>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.seniority">Seniority</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.typUmowy">Typ Umowy</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.wystawca">Wystawca</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {ogloszenieList.map((ogloszenie, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/ogloszenie/${ogloszenie.id}`} color="link" size="sm">
                        {ogloszenie.id}
                      </Button>
                    </td>
                    <td>{ogloszenie.tytul}</td>
                    <td>{ogloszenie.opis}</td>
                    <td>
                      {ogloszenie.dataPublikacji ? (
                        <TextFormat type="date" value={ogloszenie.dataPublikacji} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {ogloszenie.dataWaznosci ? (
                        <TextFormat type="date" value={ogloszenie.dataWaznosci} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {ogloszenie.startOd ? <TextFormat type="date" value={ogloszenie.startOd} format={APP_LOCAL_DATE_FORMAT} /> : null}
                    </td>
                    <td>{ogloszenie.czyWidelki ? 'true' : 'false'}</td>
                    <td>{ogloszenie.widelkiMin}</td>
                    <td>{ogloszenie.widelkiMax}</td>
                    <td>{ogloszenie.aktywne ? 'true' : 'false'}</td>
                    <td>
                      {ogloszenie.seniority ? <Link to={`/seniority/${ogloszenie.seniority.id}`}>{ogloszenie.seniority.nazwa}</Link> : ''}
                    </td>
                    <td>
                      {ogloszenie.typUmowy ? <Link to={`/typ-umowy/${ogloszenie.typUmowy.id}`}>{ogloszenie.typUmowy.tekst}</Link> : ''}
                    </td>
                    <td>
                      {ogloszenie.wystawca ? <Link to={`/wystawca/${ogloszenie.wystawca.id}`}>{ogloszenie.wystawca.nazwa}</Link> : ''}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/ogloszenie/${ogloszenie.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/ogloszenie/${ogloszenie.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/ogloszenie/${ogloszenie.id}/delete`)}
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
                <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.home.notFound">No Ogloszenies found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Ogloszenie;
