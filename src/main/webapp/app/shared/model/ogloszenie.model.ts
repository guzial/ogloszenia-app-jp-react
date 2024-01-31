import dayjs from 'dayjs';
import { ISeniority } from 'app/shared/model/seniority.model';
import { ITypUmowy } from 'app/shared/model/typ-umowy.model';
import { IWystawca } from 'app/shared/model/wystawca.model';
import { ITag } from 'app/shared/model/tag.model';

export interface IOgloszenie {
  id?: number;
  tytul?: string | null;
  opis?: string | null;
  dataPublikacji?: dayjs.Dayjs | null;
  dataWaznosci?: dayjs.Dayjs | null;
  startOd?: dayjs.Dayjs | null;
  czyWidelki?: boolean | null;
  widelkiMin?: number | null;
  widelkiMax?: number | null;
  aktywne?: boolean | null;
  seniority?: ISeniority | null;
  typUmowy?: ITypUmowy | null;
  wystawca?: IWystawca | null;
  tags?: ITag[] | null;
}

export const defaultValue: Readonly<IOgloszenie> = {
  czyWidelki: false,
  aktywne: false,
};
