import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGrupaTagow } from 'app/shared/model/grupa-tagow.model';
import { getEntities as getGrupaTagows } from 'app/entities/grupa-tagow/grupa-tagow.reducer';
import { IOgloszenie } from 'app/shared/model/ogloszenie.model';
import { getEntities as getOgloszenies } from 'app/entities/ogloszenie/ogloszenie.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntity, updateEntity, createEntity, reset } from './tag.reducer';

export const TagUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const grupaTagows = useAppSelector(state => state.grupaTagow.entities);
  const ogloszenies = useAppSelector(state => state.ogloszenie.entities);
  const tagEntity = useAppSelector(state => state.tag.entity);
  const loading = useAppSelector(state => state.tag.loading);
  const updating = useAppSelector(state => state.tag.updating);
  const updateSuccess = useAppSelector(state => state.tag.updateSuccess);

  const handleClose = () => {
    navigate('/tag');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGrupaTagows({}));
    dispatch(getOgloszenies({}));
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

    const entity = {
      ...tagEntity,
      ...values,
      grupaTagow: grupaTagows.find(it => it.id.toString() === values.grupaTagow.toString()),
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
          ...tagEntity,
          grupaTagow: tagEntity?.grupaTagow?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ogloszeniaAppReactApp.tag.home.createOrEditLabel" data-cy="TagCreateUpdateHeading">
            <Translate contentKey="ogloszeniaAppReactApp.tag.home.createOrEditLabel">Create or edit a Tag</Translate>
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
                  id="tag-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ogloszeniaAppReactApp.tag.tekst')}
                id="tag-tekst"
                name="tekst"
                data-cy="tekst"
                type="text"
              />
              <ValidatedField
                id="tag-grupaTagow"
                name="grupaTagow"
                data-cy="grupaTagow"
                label={translate('ogloszeniaAppReactApp.tag.grupaTagow')}
                type="select"
              >
                <option value="" key="0" />
                {grupaTagows
                  ? grupaTagows.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nazwaGrupy}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tag" replace color="info">
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

export default TagUpdate;
