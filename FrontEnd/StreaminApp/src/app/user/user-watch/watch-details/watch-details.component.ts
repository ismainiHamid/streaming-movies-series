import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {MovieService} from '../../../services/movie.service';
import {MovieResponse} from '../../../models/movie/movie-response';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {MediaService} from '../../../services/media.service';
import {GenreResponse} from '../../../models/genre/genre-response';
import {ActorResponse} from '../../../models/actor/actor-response';
import {MediaResponse} from '../../../models/media/media-response';

@Component({
  selector: 'app-watch-details',
  standalone: false,

  templateUrl: './watch-details.component.html',
  styleUrl: './watch-details.component.css'
})
export class WatchDetailsComponent implements OnInit {
  protected movieId: string | null = null;
  protected movieResponse: MovieResponse = new MovieResponse();
  protected genres: GenreResponse[] = [];
  protected actors: ActorResponse[] = [];
  protected mediaRecommended: MediaResponse[] = [];

  constructor(private activatedRoute: ActivatedRoute, private movieService: MovieService,
              private sanitizer: DomSanitizer, private mediaService: MediaService) {
  }

  public ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: Params): void => {
      this.movieId = params['q'] || null;
      if (this.movieId)
        this.findMovieById(this.movieId)
    });
  }

  public findMovieById(movieId: string): void {
    this.movieService.findMovieById(movieId).subscribe({
      next: (data: MovieResponse): void => {
        this.movieResponse = data;
        this.getGenres(data);
        this.produceThumbnail(data);
        this.getActors(data);
      },
      error: (err: any): void => console.error(err)
    });
  }

  public produceThumbnail(movieResponse: MovieResponse): void {
    const movieId: string | null = movieResponse.id || null;
    if (movieId)
      this.mediaService.produceMediaThumbnailById(movieId).subscribe({
        next: (data: Blob): SafeUrl => movieResponse.thumbnail = this.sanitizer.bypassSecurityTrustUrl(
          `url(${URL.createObjectURL(data)})`),
        error: (err: any): void => {
          movieResponse.isThumbnailLoaded = false;
          console.error(err);
        }, complete: (): boolean => movieResponse.isThumbnailLoaded = true
      });
  }

  public getGenres(movieResponse: MovieResponse): void {
    const movieId: string | null = movieResponse.id || null;
    if (movieId)
      this.mediaService.getGenresByMediaId(movieId).subscribe({
        next: (data: GenreResponse[]): void => {
          this.genres = data;
          this.recommendationMedia(data);
        },
        error: (err: any): void => console.error(err)
      });
  }

  public getActors(movieResponse: MovieResponse): void {
    const movieId: string | null = movieResponse.id || null;
    if (movieId)
      this.mediaService.getActorsByMediaId(movieId).subscribe({
        next: (data: ActorResponse[]): ActorResponse[] => this.actors = data,
        error: (err: any): void => console.error(err)
      });
  }

  public recommendationMedia(genres: GenreResponse[]): void {
    const genreNames: string[] = genres.map((genre: GenreResponse): string | undefined => genre?.name)
      .filter((name: string | undefined): name is string => name !== undefined && name !== null);

    if (genreNames.length > 0)
      this.mediaService.recommendationByGenres(genreNames).subscribe({
        next: (data: MediaResponse[]): void => {
          this.mediaRecommended = data.filter(
            (media: MediaResponse): boolean => media.id !== this.movieResponse.id
          );
          this.produceMediaPoster(data);
        }, error: (err: any): void => console.error(err),
      });
  }

  private produceMediaPoster(medias: MediaResponse[]): void {
    medias.forEach((media: MediaResponse): void => {
      const mediaId: string | null = media.id ?? null;
      if (mediaId)
        this.mediaService.produceMediaPosterById(mediaId).subscribe({
          next: (blob: Blob): SafeUrl => media.poster = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob)),
          error: (err: any): void => {
            console.error(err);
            media.isPosterLoaded = false;
          }, complete: (): boolean => media.isPosterLoaded = true
        });
    });
  }

  public onError(event: Event): void {
    (event.target as HTMLImageElement).src = './assets/icons/onerror.png';
  }
}
