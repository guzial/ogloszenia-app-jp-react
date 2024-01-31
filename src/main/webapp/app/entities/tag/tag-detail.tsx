import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tag.reducer';

export const TagDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tagEntity = useAppSelector(state => state.tag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tagDetailsHeading">
          <Translate contentKey="ogloszeniaAppReactApp.tag.detail.title">Tag</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tagEntity.id}</dd>
          <dt>
            <span id="tekst">
              <Translate contentKey="ogloszeniaAppReactApp.tag.tekst">Tekst</Translate>
            </span>
          </dt>
          <dd>{tagEntity.tekst}</dd>
          <dt>
            <Translate contentKey="ogloszeniaAppReactApp.tag.grupaTagow">Grupa Tagow</Translate>
          </dt>
          <dd>{tagEntity.grupaTagow ? tagEntity.grupaTagow.nazwaGrupy : ''}</dd>
        </dl>
        <Button tag={Link} to="/tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tag/${tagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TagDetail;
