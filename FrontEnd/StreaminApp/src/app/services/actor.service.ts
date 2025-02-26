import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ActorResponse} from '../models/actor/actor-response';
import {ActorRequest} from '../models/actor/actor-request';

@Injectable({
  providedIn: 'root'
})
export class ActorService {
  private uri: string = '/cinewave/api/v1/actors';

  constructor(private httpClient: HttpClient) {
  }

  public createActor(actorRequest: ActorRequest): Observable<ActorResponse> {
    return this.httpClient.post<ActorResponse>(this.uri, actorRequest);
  }

  public modifyActor(id: string, actor: FormData): Observable<ActorResponse> {
    return this.httpClient.put<ActorResponse>(`${this.uri}/${id}`, actor);
  }

  public deleteActor(id: string): Observable<ActorResponse> {
    return this.httpClient.delete<ActorResponse>(`${this.uri}/${id}`);
  }

  public findPaginatedActors(page: number, size: number, sort: string[]): Observable<ActorResponse[]> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort.join(','));
    return this.httpClient.get<ActorResponse[]>(`${this.uri}/paginated`, {params: params});
  }

  public findAllActors(): Observable<ActorResponse[]> {
    return this.httpClient.get<ActorResponse[]>(this.uri);
  }

  public findActorById(id: string): Observable<ActorResponse> {
    return this.httpClient.get<ActorResponse>(`${this.uri}/${id}`);
  }

  public findPaginatedActorByName(page: number, size: number, sort: string[], name: string): Observable<ActorResponse[]> {
    const params: HttpParams = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort.join(','))
      .set('name', name);
    return this.httpClient.get<ActorResponse[]>(`${this.uri}/filter`, {params: params});
  }

  public existsActorByName(name: string): Observable<any> {
    return this.httpClient.get<any>(`${this.uri}/${name}/exist`);
  }
}
