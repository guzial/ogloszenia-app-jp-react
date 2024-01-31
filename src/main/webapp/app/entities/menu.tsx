import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/ogloszenie">
        <Translate contentKey="global.menu.entities.ogloszenie" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/wystawca">
        <Translate contentKey="global.menu.entities.wystawca" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tag">
        <Translate contentKey="global.menu.entities.tag" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/grupa-tagow">
        <Translate contentKey="global.menu.entities.grupaTagow" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/seniority">
        <Translate contentKey="global.menu.entities.seniority" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/typ-umowy">
        <Translate contentKey="global.menu.entities.typUmowy" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
