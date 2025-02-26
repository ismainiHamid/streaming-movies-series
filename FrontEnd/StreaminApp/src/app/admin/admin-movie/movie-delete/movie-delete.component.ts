import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MovieService} from '../../../services/movie.service';

@Component({
    selector: 'app-movie-delete',
    templateUrl: './movie-delete.component.html',
    styleUrl: './movie-delete.component.css',
    standalone: false
})
export class MovieDeleteComponent implements OnInit {
  private movieId: string = '';
  protected movieTitle: string = '';
  protected isLoading: boolean = false;

  constructor(private activatedRouter: ActivatedRoute, private router: Router,
              private movieService: MovieService) {
  }

  public ngOnInit(): void {
    this.activatedRouter.queryParams.subscribe(params => {
      this.movieId = params['id'];
      this.movieTitle = params['title'];
    });
  }

  public onClickDelete(): void {
    if (this.movieId.length > 0) {
      this.isLoading = true;
      this.movieService.deleteMovie(this.movieId).subscribe({
        error: err => console.error(err),
        complete: () => {
          this.isLoading = false;
          this.closeModal();
        },
      });
    }
  }

  public closeModal(): void {
    this.router.navigate(['/admin/movies']).then();
  }
}
