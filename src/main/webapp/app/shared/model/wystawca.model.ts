import { IUser } from 'app/shared/model/user.model';

export interface IWystawca {
  id?: number;
  nazwa?: string | null;
  kontakt?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IWystawca> = {};
