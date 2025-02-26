import {Status} from '../../shared/enums/status';

export class NotificationResponse {
  public id?: string;
  public movieId?: string;
  public status?: Status;
  public progress?: number;
}
