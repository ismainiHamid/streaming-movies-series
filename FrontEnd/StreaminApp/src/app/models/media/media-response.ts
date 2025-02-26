import {SafeUrl} from '@angular/platform-browser';
import {ActorResponse} from '../actor/actor-response';

export class MediaResponse {
  public id?: string;
  public title?: string;
  public released?: string;
  public plot?: string;
  public language?: string;
  public imdbRating?: string;
  public type?: string;
  public poster?: SafeUrl;
  public isPosterLoaded: boolean = false;
  public thumbnail?: SafeUrl;
  public isThumbnailLoaded: boolean = false;
  public actors?: ActorResponse[];
  public createdAt?: Date;
}
