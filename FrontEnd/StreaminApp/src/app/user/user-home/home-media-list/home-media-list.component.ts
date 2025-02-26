import {Component, OnInit, ViewChild} from '@angular/core';
import {MovieResponse} from '../../../models/movie/movie-response';
import {MovieService} from '../../../services/movie.service';
import {MediaService} from '../../../services/media.service';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-home-media-list',
  templateUrl: './home-media-list.component.html',
  styleUrl: './home-media-list.component.css',
  standalone: false
})
export class HomeMediaListComponent implements OnInit {
  @ViewChild('owlCarousel')
  protected owlCarousel: any;
  protected customOptions: any = customOptions;
  protected moviesDataSource: MovieResponse[] = [];

  constructor(private movieService: MovieService, private mediaService: MediaService,
              private sanitizer: DomSanitizer) {
  }

  public ngOnInit(): void {
    this.findAllRecentMovies(0, 10, ['createdAt', 'DESC']);
  }

  public findAllRecentMovies(page: number, size: number, sort: string[]): void {
    this.movieService.findPaginatedMovies(page, size, sort).subscribe({
      next: (data: any): void => {
        this.moviesDataSource = data.content;
        this.produceMoviePoster(data.content);
      }, error: (err: any): void => console.error(err),
    });
  }

  private produceMoviePoster(movies: MovieResponse[]): void {
    movies.forEach((movie: MovieResponse): void => {
      const movieId: string | null = movie.id ?? null;
      if (movieId)
        this.mediaService.produceMediaPosterById(movieId).subscribe({
          next: (blob: Blob): SafeUrl => movie.poster = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob)),
          error: (err: any): void => {
            console.error(err);
            movie.isPosterLoaded = false;
          }, complete: (): boolean => movie.isPosterLoaded = true
        });
    });
  }

  public hasMasterFile(masterFile: string): boolean {
    return masterFile.length > 0;
  }

  public prev(): void {
    this.owlCarousel.prev();
  }

  public next(): void {
    this.owlCarousel.next();
  }

  public onError(event: Event): void {
    (event.target as HTMLImageElement).src = './assets/icons/onerror.png';
  }
}

export const customOptions: any = {
  loop: true,
  margin: 15,
  nav: false,
  items: 6,
  dots: false,
  autoplay: false,
  responsive: {
    0: {
      items: 3
    },
    600: {
      items: 5
    },
    1000: {
      items: 6
    }
  }
};
