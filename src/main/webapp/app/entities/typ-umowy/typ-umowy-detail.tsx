import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './typ-umowy.reducer';

export const TypUmowyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const typUmowyEntity = useAppSelector(state => state.typUmowy.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="typUmowyDetailsHeading">
          <Translate contentKey="ogloszeniaAppReactApp.typUmowy.detail.title">TypUmowy</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{typUmowyEntity.id}</dd>
          <dt>
            <span id="tekst">
              <Translate contentKey="ogloszeniaAppReactApp.typUmowy.tekst">Tekst</Translate>
            </span>
          </dt>
          <dd>{typUmowyEntity.tekst}</dd>
        </dl>
        <Button tag={Link} to="/typ-umowy" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/typ-umowy/${typUmowyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TypUmowyDetail;
