import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GenreResponse} from '../models/genre/genre-response';
import {GenreRequest} from '../models/genre/genre-request';

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private uri: string = '/cinewave/api/v1/genres';

  constructor(private httpClient: HttpClient) {
  }

  public createGenre(genreRequest: GenreRequest): Observable<GenreResponse> {
    return this.httpClient.post(this.uri, genreRequest);
  }

  public modifyGenre(id: string, genreRequest: GenreRequest): Observable<GenreResponse> {
    return this.httpClient.put<GenreResponse>(`${this.uri}/${id}`, genreRequest);
  }

  public deleteGenre(id: string): Observable<GenreResponse> {
    return this.httpClient.delete<GenreResponse>(`${this.uri}/${id}`);
  }

  public findPaginatedGenres(page: number, size: number, sort: string[]): Observable<GenreResponse[]> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort.join(','));
    return this.httpClient.get<GenreResponse[]>(`${this.uri}/paginated`, {params: params});
  }

  public findAllGenres(): Observable<GenreResponse[]> {
    return this.httpClient.get<GenreResponse[]>(this.uri);
  }

  public findGenreById(id: string): Observable<GenreResponse> {
    return this.httpClient.get<GenreResponse>(`${this.uri}/${id}`);
  }

  public findPaginatedGenreByName(page: number, size: number, sort: string[], name: string): Observable<GenreResponse[]> {
    const params: HttpParams = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort.join(','))
      .set('name', name);
    return this.httpClient.get<GenreResponse[]>(`${this.uri}/filter`, {params: params});
  }

  public existsGenreByName(name: string): Observable<any> {
    return this.httpClient.get<any>(`${this.uri}/${name}/exist`);
  }
}
