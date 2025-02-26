import {Component, OnInit} from '@angular/core';
import {MovieService} from '../../../../services/movie.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
    selector: 'app-movie-save-video',
    templateUrl: './movie-save-video.component.html',
    styleUrl: './movie-save-video.component.css',
    standalone: false
})
export class MovieSaveVideoComponent implements OnInit {
  protected isDragging = false;
  protected movieId: string | null = null;
  protected fileName: string = '';
  protected extension: string = '';
  protected file: File | null = null;
  protected isLoading: boolean = false;
  protected formGroup: any;

  constructor(private activatedRoute: ActivatedRoute, private router: Router,
              private movieService: MovieService) {
    this.formGroup = new FormGroup({
      file: new FormControl(null),
    });
  }

  public ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.movieId = params['id'];
    });
  }

  public ngSubmit(): void {
    this.router.navigate(['/admin/movies']).then(() => {
      if (this.file && this.movieId)
        this.movieService.uploadMovie(this.movieId, this.file).subscribe({
          error: error => {
            this.isLoading = false;
            console.error(error);
          }, complete: () => {
            this.isLoading = false;
          }
        });
    });
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
      const file = event.dataTransfer.files[0];
      this.handleFileSelected(file);
    }
  }

  public onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      const file = input.files[0];
      this.handleFileSelected(file);
    }
  }

  public handleFileSelected(file: File): void {
    this.extension = file.name.split('.').pop() ? '.' + file.name.split('.').pop() : '';
    if (this.extension === '.mp4') {
      this.fileName = file.name;
      this.file = file;
    }
  }

  public isFormValid(): boolean {
    return this.file !== null && this.movieId !== '';
  }

  public onRestForm(): void {
    this.file = null;
    this.formGroup.reset();
  }

  public onCloseCard(): void {
    this.fileName = '';
    this.file = null;
  }

  public closeModal(): void {
    this.router.navigate(['/admin/movies']).then();
  }
}
