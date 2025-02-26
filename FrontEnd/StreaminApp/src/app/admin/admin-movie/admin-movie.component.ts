import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {NavigationEnd, Router} from '@angular/router';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {filter} from 'rxjs';
import {MovieResponse} from '../../models/movie/movie-response';
import {MovieService} from '../../services/movie.service';
import {MediaService} from '../../services/media.service';
import {WebsocketService} from '../../services/websocket.service';
import {NotificationResponse} from '../../models/notification/notification-response';

@Component({
  selector: 'app-admin-movie',
  templateUrl: './admin-movie.component.html',
  styleUrl: './admin-movie.component.css',
  standalone: false
})
export class AdminMovieComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild(MatPaginator) protected paginator!: MatPaginator;
  protected displayedColumns: string[] = ['poster', 'title', 'released', 'runtime', 'director', 'language', 'imdbRating', 'converting', 'actions'];
  protected dataSource: MatTableDataSource<MovieResponse> = new MatTableDataSource<MovieResponse>();
  protected totalRecords: number = 0;
  protected isLoading: boolean = false;

  constructor(private movieService: MovieService, private mediaService: MediaService,
              private websocketService: WebsocketService, private router: Router,
              private sanitizer: DomSanitizer) {
  }

  public ngOnInit(): void {
    this.websocketService.connect();

    this.dataSource.paginator = this.paginator;
    this.getPaginatedMovies();

    this.router.events.pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/admin/movies')
          this.getPaginatedMovies(this.paginator.pageIndex, this.paginator.pageSize);
      });
  }

  public ngAfterViewInit(): void {
    this.paginator.page.subscribe((): void => {
      this.getPaginatedMovies(this.paginator.pageIndex, this.paginator.pageSize)
    });
  }

  public getPaginatedMovies(page: number = 0, pageSize: number = 5): void {
    this.isLoading = true;
    this.movieService.findPaginatedMovies(page, pageSize, ['createdAt', 'DESC']).subscribe({
      next: (data: any): void => {
        this.dataSource.data = data.content;
        this.totalRecords = data.page.totalElements;
        this.getMovieProgress(data.content);
        this.getMoviePoster(data.content);
      }, error: (err: any): void => console.error(err),
      complete: (): boolean => this.isLoading = false
    });
  }

  public getMoviePoster(movies: MovieResponse[]): void {
    movies.forEach((movie: MovieResponse): void => {
      const movieId: string | null = movie.id ?? null;
      if (movieId != null)
        this.mediaService.produceMediaPosterById(movieId).subscribe({
          next: (blob: Blob): SafeUrl => movie.poster = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob)),
          error: (err: any): void => {
            movie.isPosterLoaded = false;
            console.error(err);
          }, complete: (): boolean => movie.isPosterLoaded = true
        });
    });
  }

  public getMovieProgress(movies: MovieResponse[]): void {
    movies.forEach((movie: MovieResponse): void => {
      const movieId: string | null = movie.id ?? null;
      if (movieId != null)
        this.websocketService.getMessages().subscribe((notification: NotificationResponse): void => {
          console.log(notification);
          const notifyMovieId: string | null = notification.movieId || null;
          if (notifyMovieId !== null)
            if (movieId === notifyMovieId) {
              movie.progress = notification.progress;
              movie.status = notification.status;
            }
        });
    });
  }

  public filterByTitle(page: number = 0, pageSize: number = 5, title: string): void {
    this.isLoading = true;
    this.movieService.findPaginatedByMovieTitle(page, pageSize, ['createdAt', 'DESC'], title).subscribe({
      next: (data: any): void => {
        this.dataSource.data = data.content;
        this.totalRecords = data.page.totalElements;
        this.getMoviePoster(data.content);
        this.getMovieProgress(data.content);
      }, error: (err: any): void => console.error(err),
      complete: (): boolean => this.isLoading = false
    });
  }

  public applyFilter(event: any): void {
    const filterValue: string = (event.target as HTMLInputElement).value.trim().toLowerCase();
    if (filterValue !== '') this.filterByTitle(0, 5, filterValue);
    else this.getPaginatedMovies(this.paginator.pageIndex, this.paginator.pageSize);
  }

  public resetFilter(event: any): void {
    event.value = '';
    this.getPaginatedMovies(this.paginator.pageIndex, this.paginator.pageSize);
  }

  public onError(event: Event): void {
    (event.target as HTMLImageElement).src = './assets/icons/onerror.png';
  }

  public ngOnDestroy(): void {
    this.websocketService.stopConnection();
  }
}
