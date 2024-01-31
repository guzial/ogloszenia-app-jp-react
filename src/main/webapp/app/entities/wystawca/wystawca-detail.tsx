import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './wystawca.reducer';

export const WystawcaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const wystawcaEntity = useAppSelector(state => state.wystawca.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="wystawcaDetailsHeading">
          <Translate contentKey="ogloszeniaAppReactApp.wystawca.detail.title">Wystawca</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{wystawcaEntity.id}</dd>
          <dt>
            <span id="nazwa">
              <Translate contentKey="ogloszeniaAppReactApp.wystawca.nazwa">Nazwa</Translate>
            </span>
          </dt>
          <dd>{wystawcaEntity.nazwa}</dd>
          <dt>
            <span id="kontakt">
              <Translate contentKey="ogloszeniaAppReactApp.wystawca.kontakt">Kontakt</Translate>
            </span>
          </dt>
          <dd>{wystawcaEntity.kontakt}</dd>
          <dt>
            <Translate contentKey="ogloszeniaAppReactApp.wystawca.user">User</Translate>
          </dt>
          <dd>{wystawcaEntity.user ? wystawcaEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/wystawca" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/wystawca/${wystawcaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WystawcaDetail;
