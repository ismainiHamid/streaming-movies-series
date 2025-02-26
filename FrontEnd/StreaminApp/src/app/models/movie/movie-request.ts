import {MediaRequest} from '../media/media-request';

export class MovieRequest extends MediaRequest {
  public runtime?: string;
  public director?: string;
}
