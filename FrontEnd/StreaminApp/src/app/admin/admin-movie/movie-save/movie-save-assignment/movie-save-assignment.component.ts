import {Component, OnInit} from '@angular/core';
import {GenreResponse} from '../../../../models/genre/genre-response';
import {ActorResponse} from '../../../../models/actor/actor-response';
import {GenreService} from '../../../../services/genre.service';
import {ActorService} from '../../../../services/actor.service';
import {FormControl, FormGroup} from '@angular/forms';
import {ErrorResponse} from '../../../../models/exception/error-response';
import {ActivatedRoute, Router} from '@angular/router';
import {MediaService} from '../../../../services/media.service';
import {forkJoin} from 'rxjs';

@Component({
  selector: 'app-movie-save-assignment',
  templateUrl: './movie-save-assignment.component.html',
  styleUrl: './movie-save-assignment.component.css',
  standalone: false
})
export class MovieSaveAssignmentComponent implements OnInit {
  protected genres: GenreResponse[] = [];
  protected genresIdsSelected: Set<string> = new Set();
  protected genresGetList: string[] = [];
  protected actors: ActorResponse[] = [];
  protected actorsIdsSelected: Set<string> = new Set();
  protected actorsGetList: string[] = [];
  protected movieId: string | null = null;
  protected error: ErrorResponse = new ErrorResponse();
  protected isLoading: boolean = false;
  protected isGenreLoading: boolean = true;
  protected isActorLoading: boolean = true;
  protected formGroup: any;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private genreService: GenreService, private actorService: ActorService,
              private mediaService: MediaService) {
    this.formGroup = new FormGroup({
      genres: new FormControl(null),
      actors: new FormControl(null),
    });
  }

  public ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.movieId = params['id'];
      this.genresGetList = params['genres'].split(',');
      this.actorsGetList = params['actors'].split(',');
    });
    this.checkGenreInDatabaseOrSave();
    this.findAllGenres();
    this.checkActorInDatabaseOrSave();
    this.findAllActors();
  }

  public ngSubmit(): void {
    if (this.genresIdsSelected.size > 0 && this.actorsIdsSelected.size > 0) {
      if (this.movieId !== null) {
        this.isLoading = true;
        const addGenres$ = this.mediaService.addGenresToMedia(this.movieId, Array.from(this.genresIdsSelected));
        const addActors$ = this.mediaService.addActorsToMedia(this.movieId, Array.from(this.actorsIdsSelected));

        forkJoin([addGenres$, addActors$]).subscribe({
          error: (err) => console.error(err),
          complete: () => {
            this.isLoading = false;
            this.router.navigate(['/admin/movies/save/upload'], {
              queryParams: {id: this.movieId}
            }).then();
          }
        });
      }
    }
  }

  public findAllGenres(): void {
    this.genreService.findAllGenres().subscribe({
      next: (data: any) => {
        this.genres = data;
        this.onGenreFindCheckBoxChange(data);
      },
      error: err => {
        this.genres = [];
        console.error(err);
      }
    });
  }

  public filterGenreByName(event: any): void {
    const input: HTMLInputElement = event.target as HTMLInputElement;
    if (input.value.trim() !== '')
      this.genreService.findPaginatedGenreByName(0, 5, [], input.value.toLocaleLowerCase()).subscribe({
        next: data => this.genres = data,
        error: err => {
          this.genres = [];
          console.error(err);
        },
      });
    else this.findAllGenres();
  }

  public onGenreFindCheckBoxChange(genres: GenreResponse[]): void {
    genres.forEach(genre => {
      const result: string | null = this.genresGetList.find(val =>
        val.trim().toLocaleLowerCase() === genre.name?.trim().toLowerCase()
      ) || null;

      if (result !== null) {
        const genreExist: GenreResponse | null = this.genres.find(val =>
          val.name?.trim().toLocaleLowerCase() === result.trim().toLocaleLowerCase()
        ) || null;

        if (genreExist !== null)
          this.genresIdsSelected.add(genreExist.id ?? '');
      }
    });
    this.isGenreLoading = false;
  }

  public checkGenreInDatabaseOrSave(): void {
    const result: string[] = this.genresGetList;
    if (result.length > 0) {
      result.forEach(name => {
        this.genreService.existsGenreByName(name.trim()).subscribe({
          next: (data: any): void => {
            if (!data.data.exists) {
              const actorRequest: ActorResponse = {name: name.trim()}
              this.genreService.createGenre(actorRequest).subscribe({
                error: err => console.error(err),
              });
            }
          }, error: err => console.error(err),
          complete: () => this.findAllActors()
        });
      });
    }
  }

  public onGenreCheckboxChange(event: any, id: string): void {
    if (event.target.checked)
      this.genresIdsSelected.add(id);
    else this.genresIdsSelected.delete(id);
  }

  public isGenreChecked(genreId: string): boolean {
    return this.genresIdsSelected.has(genreId);
  }

  public findAllActors(): void {
    this.actorService.findAllActors().subscribe({
      next: (data: any) => {
        this.actors = data;
        this.onActorFindCheckboxChange(data);
      },
      error: err => {
        this.actors = [];
        console.error(err);
      }
    });
  }

  public filterActorByName(event: any): void {
    const input: HTMLInputElement = event.target as HTMLInputElement;
    if (input.value.trim() !== '')
      this.actorService.findPaginatedActorByName(0, 5, [], input.value.toLocaleLowerCase()).subscribe({
        next: data => this.actors = data,
        error: err => {
          this.actors = [];
          console.error(err);
        },
      });
    else this.findAllActors();
  }

  public onActorFindCheckboxChange(actors: ActorResponse[]): void {
    actors.forEach(actor => {
      const result: string | null = this.actorsGetList.find(val =>
        val.trim().toLocaleLowerCase() === actor.name?.trim().toLowerCase()
      ) || null;

      if (result !== null) {
        const actorExist: ActorResponse | null = this.actors.find(val =>
          val.name?.trim().toLocaleLowerCase() === result.trim().toLocaleLowerCase()
        ) || null;

        if (actorExist !== null)
          this.actorsIdsSelected.add(actorExist.id ?? '');
      }
    });
    this.isActorLoading = false;
  }

  public checkActorInDatabaseOrSave(): void {
    const result: string[] = this.actorsGetList;
    if (result.length > 0) {
      result.forEach(name => {
        this.actorService.existsActorByName(name.trim()).subscribe({
          next: (data: any): void => {
            if (!data.data.exists) {
              const actorRequest: ActorResponse = {name: name.trim()}
              this.actorService.createActor(actorRequest).subscribe({
                error: err => console.error(err),
              });
            }
          }, error: err => console.error(err),
          complete: () => this.findAllActors()
        });
      });
    }
  }

  public onActorCheckboxChange(event: any, id: string): void {
    if (event.target.checked)
      this.actorsIdsSelected.add(id);
    else this.actorsIdsSelected.delete(id);
  }

  public isActorChecked(actorId: string): boolean {
    return this.actorsIdsSelected.has(actorId);
  }

  public isFormValid(): boolean {
    return this.genresIdsSelected.size > 0 && this.actorsIdsSelected.size > 0;
  }

  public onRestForm(): void {
    this.isGenreLoading = true;
    this.isActorLoading = true;
    this.genresIdsSelected.clear();
    this.actorsIdsSelected.clear();
    this.findAllGenres();
    this.findAllActors();
    this.formGroup.reset();
  }

  public closeModal(): void {
    this.router.navigate(['/admin/movies']).then();
  }
}
