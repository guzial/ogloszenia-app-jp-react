import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './grupa-tagow.reducer';

export const GrupaTagowDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const grupaTagowEntity = useAppSelector(state => state.grupaTagow.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="grupaTagowDetailsHeading">
          <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.detail.title">GrupaTagow</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{grupaTagowEntity.id}</dd>
          <dt>
            <span id="nazwaGrupy">
              <Translate contentKey="ogloszeniaAppReactApp.grupaTagow.nazwaGrupy">Nazwa Grupy</Translate>
            </span>
          </dt>
          <dd>{grupaTagowEntity.nazwaGrupy}</dd>
        </dl>
        <Button tag={Link} to="/grupa-tagow" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/grupa-tagow/${grupaTagowEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GrupaTagowDetail;
