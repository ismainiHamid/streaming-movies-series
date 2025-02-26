import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {lastValueFrom, Observable} from 'rxjs';
import {ImdbResponse} from '../models/imdb/imdb-response';

@Injectable({
  providedIn: 'root'
})
export class ImdbService {
  private uri: string = '/cinewave/api/v1/imdb'

  constructor(private httpClient: HttpClient) {
  }

  public getMovieDetails(imdbId: string): Observable<ImdbResponse> {
    return this.httpClient.get<ImdbResponse>(`${this.uri}/${imdbId}`);
  }

  public async convertImageUrlToFile(url: string, fileName: string): Promise<File> {
    const blob = await lastValueFrom(
      this.httpClient.get(url, {responseType: 'blob'})
    );
    return new File([blob], fileName, {type: blob.type});
  }
}
