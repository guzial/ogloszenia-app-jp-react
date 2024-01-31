import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISeniority } from 'app/shared/model/seniority.model';
import { getEntities as getSeniorities } from 'app/entities/seniority/seniority.reducer';
import { ITypUmowy } from 'app/shared/model/typ-umowy.model';
import { getEntities as getTypUmowies } from 'app/entities/typ-umowy/typ-umowy.reducer';
import { IWystawca } from 'app/shared/model/wystawca.model';
import { getEntities as getWystawcas } from 'app/entities/wystawca/wystawca.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IOgloszenie } from 'app/shared/model/ogloszenie.model';
import { getEntity, updateEntity, createEntity, reset } from './ogloszenie.reducer';

export const OgloszenieUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const seniorities = useAppSelector(state => state.seniority.entities);
  const typUmowies = useAppSelector(state => state.typUmowy.entities);
  const wystawcas = useAppSelector(state => state.wystawca.entities);
  const tags = useAppSelector(state => state.tag.entities);
  const ogloszenieEntity = useAppSelector(state => state.ogloszenie.entity);
  const loading = useAppSelector(state => state.ogloszenie.loading);
  const updating = useAppSelector(state => state.ogloszenie.updating);
  const updateSuccess = useAppSelector(state => state.ogloszenie.updateSuccess);

  const handleClose = () => {
    navigate('/ogloszenie');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getSeniorities({}));
    dispatch(getTypUmowies({}));
    dispatch(getWystawcas({}));
    dispatch(getTags({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.widelkiMin !== undefined && typeof values.widelkiMin !== 'number') {
      values.widelkiMin = Number(values.widelkiMin);
    }
    if (values.widelkiMax !== undefined && typeof values.widelkiMax !== 'number') {
      values.widelkiMax = Number(values.widelkiMax);
    }

    const entity = {
      ...ogloszenieEntity,
      ...values,
      tags: mapIdList(values.tags),
      seniority: seniorities.find(it => it.id.toString() === values.seniority.toString()),
      typUmowy: typUmowies.find(it => it.id.toString() === values.typUmowy.toString()),
      wystawca: wystawcas.find(it => it.id.toString() === values.wystawca.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...ogloszenieEntity,
          seniority: ogloszenieEntity?.seniority?.id,
          typUmowy: ogloszenieEntity?.typUmowy?.id,
          wystawca: ogloszenieEntity?.wystawca?.id,
          tags: ogloszenieEntity?.tags?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ogloszeniaAppReactApp.ogloszenie.home.createOrEditLabel" data-cy="OgloszenieCreateUpdateHeading">
            <Translate contentKey="ogloszeniaAppReactApp.ogloszenie.home.createOrEditLabel">Create or edit a Ogloszenie</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="ogloszenie-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.tytul')}
                id="ogloszenie-tytul"
                name="tytul"
                data-cy="tytul"
                type="text"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.opis')}
                id="ogloszenie-opis"
                name="opis"
                data-cy="opis"
                type="text"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.dataPublikacji')}
                id="ogloszenie-dataPublikacji"
                name="dataPublikacji"
                data-cy="dataPublikacji"
                type="date"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.dataWaznosci')}
                id="ogloszenie-dataWaznosci"
                name="dataWaznosci"
                data-cy="dataWaznosci"
                type="date"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.startOd')}
                id="ogloszenie-startOd"
                name="startOd"
                data-cy="startOd"
                type="date"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.czyWidelki')}
                id="ogloszenie-czyWidelki"
                name="czyWidelki"
                data-cy="czyWidelki"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.widelkiMin')}
                id="ogloszenie-widelkiMin"
                name="widelkiMin"
                data-cy="widelkiMin"
                type="text"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.widelkiMax')}
                id="ogloszenie-widelkiMax"
                name="widelkiMax"
                data-cy="widelkiMax"
                type="text"
              />
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.aktywne')}
                id="ogloszenie-aktywne"
                name="aktywne"
                data-cy="aktywne"
                check
                type="checkbox"
              />
              <ValidatedField
                id="ogloszenie-seniority"
                name="seniority"
                data-cy="seniority"
                label={translate('ogloszeniaAppReactApp.ogloszenie.seniority')}
                type="select"
              >
                <option value="" key="0" />
                {seniorities
                  ? seniorities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nazwa}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ogloszenie-typUmowy"
                name="typUmowy"
                data-cy="typUmowy"
                label={translate('ogloszeniaAppReactApp.ogloszenie.typUmowy')}
                type="select"
              >
                <option value="" key="0" />
                {typUmowies
                  ? typUmowies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.tekst}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ogloszenie-wystawca"
                name="wystawca"
                data-cy="wystawca"
                label={translate('ogloszeniaAppReactApp.ogloszenie.wystawca')}
                type="select"
              >
                <option value="" key="0" />
                {wystawcas
                  ? wystawcas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nazwa}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.ogloszenie.tag')}
                id="ogloszenie-tag"
                data-cy="tag"
                type="select"
                multiple
                name="tags"
              >
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.tekst}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ogloszenie" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OgloszenieUpdate;
