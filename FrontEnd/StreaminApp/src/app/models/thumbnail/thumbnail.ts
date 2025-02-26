import {SafeUrl} from '@angular/platform-browser';

export class Thumbnail {
  public id?: string;
  public filename?: string;
  public thumbnail?: SafeUrl;
  public isLoaded: boolean = false;
  public isChecked: boolean = false;
}
