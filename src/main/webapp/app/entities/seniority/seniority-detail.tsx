import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seniority.reducer';

export const SeniorityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const seniorityEntity = useAppSelector(state => state.seniority.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seniorityDetailsHeading">
          <Translate contentKey="ogloszeniaAppReactApp.seniority.detail.title">Seniority</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{seniorityEntity.id}</dd>
          <dt>
            <span id="nazwa">
              <Translate contentKey="ogloszeniaAppReactApp.seniority.nazwa">Nazwa</Translate>
            </span>
          </dt>
          <dd>{seniorityEntity.nazwa}</dd>
        </dl>
        <Button tag={Link} to="/seniority" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seniority/${seniorityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeniorityDetail;
