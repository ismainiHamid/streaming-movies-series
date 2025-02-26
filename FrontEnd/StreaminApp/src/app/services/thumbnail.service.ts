import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Thumbnail} from '../models/thumbnail/thumbnail';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ThumbnailService {
  private uri: string = '/cinewave/api/v1/thumbnails';

  constructor(private httpClient: HttpClient) {
  }

  public findPaginatedThumbnailsByFileName(page: number, size: number, filename: string): Observable<Thumbnail[]> {
    const params: HttpParams = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('filename', filename);
    return this.httpClient.get<Thumbnail[]>(`${this.uri}/paginated`, {params: params});
  }

  public deleteThumbnail(thumbnailId: string): Observable<Thumbnail> {
    return this.httpClient.delete<Thumbnail>(`${this.uri}/${thumbnailId}`);
  }

  public produceThumbnailById(thumbnailId: string): Observable<Blob> {
    return this.httpClient.get<Blob>(`${this.uri}/${thumbnailId}/thumbnail`, {responseType: 'blob' as 'json'});
  }
}
