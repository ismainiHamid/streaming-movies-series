import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GenreResponse} from '../models/genre/genre-response';
import {ActorResponse} from '../models/actor/actor-response';
import {MediaResponse} from '../models/media/media-response';

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  private uri: string = '/cinewave/api/v1/medias'

  constructor(private httpClient: HttpClient) {
  }

  public produceMediaPosterById(id: string): Observable<Blob> {
    return this.httpClient.get<Blob>(`${this.uri}/${id}/poster`, {responseType: 'blob' as 'json'});
  }

  public produceMediaThumbnailById(id: string): Observable<Blob> {
    return this.httpClient.get<Blob>(`${this.uri}/${id}/thumbnail`, {responseType: 'blob' as 'json'});
  }

  public addGenresToMedia(mediaId: string, genreIds: string[]): Observable<void> {
    return this.httpClient.patch<void>(`${this.uri}/${mediaId}/genres`, genreIds);
  }

  public removeGenresFromMedia(mediaId: string, genreIds: string[]): Observable<void> {
    const params: HttpParams = new HttpParams()
      .set('genreIds', genreIds.join(','));
    return this.httpClient.delete<void>(`${this.uri}/${mediaId}/genres`, {params: params});
  }

  public getGenresByMediaId(mediaId: string): Observable<GenreResponse[]> {
    return this.httpClient.get<GenreResponse[]>(`${this.uri}/${mediaId}/genres`);
  }

  public addActorsToMedia(mediaId: string, actorIds: string[]): Observable<void> {
    return this.httpClient.patch<void>(`${this.uri}/${mediaId}/actors`, actorIds);
  }

  public removeActorsFromMedia(mediaId: string, actorIds: string[]): Observable<void> {
    const params: HttpParams = new HttpParams()
      .set('actorIds', actorIds.join(','));
    return this.httpClient.delete<void>(`${this.uri}/${mediaId}/actors`);
  }

  public getActorsByMediaId(mediaId: string): Observable<ActorResponse[]> {
    return this.httpClient.get<ActorResponse[]>(`${this.uri}/${mediaId}/actors`);
  }

  public findPaginatedMediaByTitle(page: number, size: number, sort: string[], title: string): Observable<MediaResponse[]> {
    const params: HttpParams = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort.join(','))
      .set('title', title);
    return this.httpClient.get<MediaResponse[]>(`${this.uri}/filter`, {params: params});
  }

  public recommendationByGenres(genres: string[]): Observable<MediaResponse[]> {
    const params: HttpParams = new HttpParams()
      .set('genres', genres.join(','));
    return this.httpClient.get<MediaResponse[]>(`${this.uri}/recommendation`, {params: params});
  }

  public getMasterFileByMediaId(id: string): Observable<{ segment: string }> {
    return this.httpClient.get<{ segment: string }>(`${this.uri}/${id}`);
  }
}
