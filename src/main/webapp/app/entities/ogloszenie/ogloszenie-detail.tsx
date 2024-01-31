import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ogloszenie.reducer';

export const OgloszenieDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ogloszenieEntity = useAppSelector(state => state.ogloszenie.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ogloszenieDetailsHeading">
          <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.detail.title">Ogloszenie</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ogloszenieEntity.id}</dd>
          <dt>
            <span id="tytul">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.tytul">Tytul</Translate>
            </span>
          </dt>
          <dd>{ogloszenieEntity.tytul}</dd>
          <dt>
            <span id="opis">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.opis">Opis</Translate>
            </span>
          </dt>
          <dd>{ogloszenieEntity.opis}</dd>
          <dt>
            <span id="dataPublikacji">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.dataPublikacji">Data Publikacji</Translate>
            </span>
          </dt>
          <dd>
            {ogloszenieEntity.dataPublikacji ? (
              <TextFormat value={ogloszenieEntity.dataPublikacji} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dataWaznosci">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.dataWaznosci">Data Waznosci</Translate>
            </span>
          </dt>
          <dd>
            {ogloszenieEntity.dataWaznosci ? (
              <TextFormat value={ogloszenieEntity.dataWaznosci} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="startOd">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.startOd">Start Od</Translate>
            </span>
          </dt>
          <dd>
            {ogloszenieEntity.startOd ? <TextFormat value={ogloszenieEntity.startOd} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="czyWidelki">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.czyWidelki">Czy Widelki</Translate>
            </span>
          </dt>
          <dd>{ogloszenieEntity.czyWidelki ? 'true' : 'false'}</dd>
          <dt>
            <span id="widelkiMin">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.widelkiMin">Widelki Min</Translate>
            </span>
          </dt>
          <dd>{ogloszenieEntity.widelkiMin}</dd>
          <dt>
            <span id="widelkiMax">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.widelkiMax">Widelki Max</Translate>
            </span>
          </dt>
          <dd>{ogloszenieEntity.widelkiMax}</dd>
          <dt>
            <span id="aktywne">
              <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.aktywne">Aktywne</Translate>
            </span>
          </dt>
          <dd>{ogloszenieEntity.aktywne ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.seniority">Seniority</Translate>
          </dt>
          <dd>{ogloszenieEntity.seniority ? ogloszenieEntity.seniority.nazwa : ''}</dd>
          <dt>
            <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.typUmowy">Typ Umowy</Translate>
          </dt>
          <dd>{ogloszenieEntity.typUmowy ? ogloszenieEntity.typUmowy.tekst : ''}</dd>
          <dt>
            <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.wystawca">Wystawca</Translate>
          </dt>
          <dd>{ogloszenieEntity.wystawca ? ogloszenieEntity.wystawca.nazwa : ''}</dd>
          <dt>
            <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.tag">Tag</Translate>
          </dt>
          <dd>
            {ogloszenieEntity.tags
              ? ogloszenieEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.tekst}</a>
                    {ogloszenieEntity.tags && i === ogloszenieEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/ogloszenie" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ogloszenie/${ogloszenieEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OgloszenieDetail;
