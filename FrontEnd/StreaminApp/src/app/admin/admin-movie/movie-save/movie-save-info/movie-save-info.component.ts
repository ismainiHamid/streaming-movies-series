import {Component} from '@angular/core';
import {MovieService} from '../../../../services/movie.service';
import {Router} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';
import {ErrorResponse} from '../../../../models/exception/error-response';
import {ImdbService} from '../../../../services/imdb.service';
import {ImdbResponse} from '../../../../models/imdb/imdb-response';
import {SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-movie-save-info',
  templateUrl: './movie-save-info.component.html',
  styleUrl: './movie-save-info.component.css',
  standalone: false
})
export class MovieSaveInfoComponent {
  protected LANGUAGES: { name: string }[] = LANGUAGES;
  protected selectedPoster: File | null = null;
  protected safeUrlPoster: SafeUrl | null = null;
  protected selectedThumbnailFileName: string | null = null;
  protected selectedThumbnail: File | null = null;
  protected selectedLanguages: string[] = [];
  protected isDragging: boolean = false;
  protected isImdbLoading: boolean = false;
  protected fileName: string = '';
  protected movieId: string | null = null;
  protected error: ErrorResponse = new ErrorResponse();
  protected isLoading: boolean = false;
  protected imdbResponse: ImdbResponse | null = null;
  protected formGroup: any;

  constructor(private movieService: MovieService, private imdbService: ImdbService,
              private router: Router) {
    this.formGroup = new FormGroup({
      imdbId: new FormControl(null),
      imdbRating: new FormControl(null),
      title: new FormControl(null),
      released: new FormControl(null),
      runtime: new FormControl(null),
      director: new FormControl(null),
      plot: new FormControl(null),
    });
  }

  public ngSubmit(): void {
    this.isLoading = true;
    if (this.formGroup.value) {
      if (this.selectedPoster !== null && this.selectedThumbnail !== null) {
        let movie: FormData = new FormData();
        movie.append('title', this.formGroup.get('title')?.value);
        movie.append('released', this.formGroup.get('released')?.value);
        movie.append('runtime', this.formGroup.get('runtime')?.value);
        movie.append('director', this.formGroup.get('director')?.value);
        movie.append('language', this.selectedLanguages.join(', '));
        movie.append('plot', this.formGroup.get('plot')?.value);
        movie.append('poster', this.selectedPoster);
        movie.append('thumbnail', this.selectedThumbnail);
        movie.append('imdbRating', this.formGroup.get('imdbRating')?.value);

        this.movieService.createMovie(movie).subscribe({
          next: movie => this.movieId = movie.id ?? null,
          error: err => {
            this.isLoading = false;
            this.error = err.error;
          }, complete: () => {
            this.isLoading = false;
            this.router.navigate(['/admin/movies/save/assignment'], {
              queryParams: {
                id: this.movieId,
                genres: this.imdbResponse?.Genre,
                actors: this.imdbResponse?.Actors
              }
            }).then();
          },
        });
      }
    }
  }

  public getMovieDetailsByImdbId(): void {
    this.isImdbLoading = true;
    const imdbId: string | null = this.formGroup.get('imdbId')?.value;
    if (imdbId !== null) {
      this.imdbService.getMovieDetails(imdbId).subscribe({
        next: (data: ImdbResponse): void => {
          if (data.Response?.toLocaleLowerCase() === 'true') {
            if (data.Type?.toLocaleLowerCase() === 'movie') {
              this.formGroup.get('title').setValue(data.Title);
              this.formGroup.get('released').setValue(this.parseDate(data.Released ?? null));
              this.formGroup.get('runtime').setValue(data.Runtime);
              this.formGroup.get('director').setValue(data.Director);
              this.selectedLanguages = this.parseLanguages(data.Language ?? null);
              this.formGroup.get('plot').setValue(data.Plot);
              this.formGroup.get('imdbRating').setValue(data.imdbRating)
              this.imdbService.convertImageUrlToFile(
                data.Poster,
                data.Title?.replace(' ', '_').toLocaleLowerCase() + '_poster'
              ).then((poster: File): void => {
                this.selectedPoster = poster;
                this.fileName = data.Title?.replace(' ', '_').toLocaleLowerCase() + '_poster';
                this.getPosterBackground(poster);
              });
              this.imdbResponse = data;
              this.isImdbLoading = false;
            } else {
              this.onRestForm();
              this.formGroup.get('imdbId').setValue(imdbId);
              this.isImdbLoading = true;
              this.error.code = 402;
              this.error.message = "The id: " + imdbId + ", is not a movie.";
            }
          }
        },
        error: err => {
          this.onRestForm();
          this.formGroup.get('imdbId').setValue(imdbId);
          this.isImdbLoading = true;
          this.error.code = err.status;
          this.error.message = "The id: " + imdbId + ", not found in imdb.";
        },
      });
    }
  }

  public parseLanguages(languages: string | null): string[] | [] {
    if (languages === null) return [];
    this.selectedLanguages = [];
    return languages.split(',').map(lang => lang.trim().toLocaleLowerCase());
  }

  public onAddLanguage(event: any): void {
    const input: HTMLInputElement = event.target as HTMLInputElement;
    if (this.selectedLanguages.length < 4)
      this.selectedLanguages.push(input.value);
    input.value = '';
  }

  public onRemoveLanguage(language: string): void {
    this.selectedLanguages = this.selectedLanguages.filter(lang =>
      lang.trim().toLocaleLowerCase() !== language.trim().toLocaleLowerCase()
    );
  }

  public parseDate(dateString: string | null): string | null {
    if (!dateString) return null;
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return null;
    const year: number = date.getFullYear();
    const month: string = String(date.getMonth() + 1).padStart(2, '0');
    const day: string = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  public onKeyUpImdbID(): void {
    this.isImdbLoading = false;
    this.error = new ErrorResponse();
  }

  public onKeyUpTitle(): void {
    this.error = new ErrorResponse();
  }

  public closeModal(): void {
    this.router.navigate(['/admin/movies']).then();
  }

  public onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = true;
  }

  public onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = false;
  }

  public onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragging = false;
    if (event.dataTransfer?.files) {
      const file: File = event.dataTransfer.files[0];
      this.handleFileSelected(file);
    }
  }

  public onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      const file: File = input.files[0];
      this.handleFileSelected(file);
    }
  }

  public handleFileSelected(file: File): void {
    this.fileName = file.name;
    this.selectedPoster = file;
    this.getPosterBackground(file);
  }

  public getPosterBackground(poster: File): void {
    this.safeUrlPoster = URL.createObjectURL(poster);
  }

  public onFileSelectedThumbnail(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedThumbnailFileName = input.files[0].name;
      this.selectedThumbnail = input.files[0];
    } else this.selectedThumbnailFileName = null;
  }

  public isFormValid(): boolean {
    return this.formGroup.valid && this.selectedPoster !== null && this.selectedLanguages.length > 0 && this.selectedThumbnail !== null;
  }

  public isImdbInputValid(): boolean {
    const value: string | null = this.formGroup.get('imdbId')?.value;
    return !(value === null || value === undefined || value === '');

  }

  public onRestForm(): void {
    this.formGroup.reset();
    this.fileName = '';
    this.selectedLanguages = [];
    this.selectedPoster = null;
    this.isImdbLoading = false;
    this.isDragging = false;
    this.error = new ErrorResponse();
    this.isFormValid();
  }

  public onCloseCard(): void {
    this.fileName = '';
    this.selectedPoster = null;
  }
}

export const LANGUAGES: { name: string }[] = [
  {name: 'English'},
  {name: 'Spanish'},
  {name: 'French'},
  {name: 'German'},
  {name: 'Chinese'},
  {name: 'Japanese'},
  {name: 'Russian'},
  {name: 'Arabic'},
  {name: 'Hindi'},
  {name: 'Portuguese'},
  {name: 'Bengali'},
  {name: 'Korean'},
  {name: 'Italian'},
  {name: 'Turkish'},
  {name: 'Polish'},
  {name: 'Dutch'},
  {name: 'Swedish'},
  {name: 'Ukrainian'},
  {name: 'Vietnamese'},
  {name: 'Mandarin'},
  {name: 'Thai'}
];
