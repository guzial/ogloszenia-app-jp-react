import { IGrupaTagow } from 'app/shared/model/grupa-tagow.model';
import { IOgloszenie } from 'app/shared/model/ogloszenie.model';

export interface ITag {
  id?: number;
  tekst?: string | null;
  grupaTagow?: IGrupaTagow | null;
  ogloszenies?: IOgloszenie[] | null;
}

export const defaultValue: Readonly<ITag> = {};
