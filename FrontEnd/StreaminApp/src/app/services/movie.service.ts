import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {MovieResponse} from '../models/movie/movie-response';
import {MovieRequest} from '../models/movie/movie-request';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private uri: string = '/cinewave/api/v1/movies';

  constructor(private httpClient: HttpClient) {
  }

  public createMovie(movieRequest: FormData): Observable<MovieResponse> {
    return this.httpClient.post<MovieResponse>(this.uri, movieRequest);
  }

  public modifyMovie(id: string, movieRequest: MovieRequest): Observable<MovieResponse> {
    return this.httpClient.put<MovieResponse>(`${this.uri}/${id}`, movieRequest);
  }

  public deleteMovie(id: string): Observable<MovieResponse> {
    return this.httpClient.delete<MovieResponse>(`${this.uri}/${id}`);
  }

  public findPaginatedMovies(page: number, size: number, sort: string[]): Observable<MovieResponse[]> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort.join(','));
    return this.httpClient.get<MovieResponse[]>(`${this.uri}/paginated`, {params: params});
  }

  public findMovieById(id: string): Observable<MovieResponse> {
    return this.httpClient.get<MovieResponse>(`${this.uri}/${id}`);
  }

  public findPaginatedByMovieTitle(page: number, size: number, sort: string[], title: string): Observable<MovieResponse[]> {
    const params: HttpParams = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort.join(','))
      .set('title', title);
    return this.httpClient.get<MovieResponse[]>(`${this.uri}/filter`, {params: params});
  }

  public uploadMovie(id: string, file: File): Observable<HttpEvent<MovieResponse>> {
    const formData = new FormData();
    formData.append('movieFile', file);
    return this.httpClient.post<MovieResponse>(`${this.uri}/${id}/upload`, formData, {
      observe: 'events',
      reportProgress: true
    });
  }
}
