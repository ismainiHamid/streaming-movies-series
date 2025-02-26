import {Status} from '../../shared/enums/status';
import {MediaResponse} from '../media/media-response';

export class MovieResponse extends MediaResponse {
  public runtime?: string;
  public director?: string;
  public masterFile?: string;
  public status?: Status;
  public quality?: string;
  public progress?: number = 100;
}
