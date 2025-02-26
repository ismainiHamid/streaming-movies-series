import {Component, ElementRef, HostListener, ViewChild} from '@angular/core';
import {MediaService} from '../../../services/media.service';
import {MediaResponse} from '../../../models/media/media-response';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {ActorResponse} from '../../../models/actor/actor-response';

@Component({
  selector: 'app-user-navbar',
  standalone: false,

  templateUrl: './user-navbar.component.html',
  styleUrl: './user-navbar.component.css'
})
export class UserNavbarComponent {
  @ViewChild('filter') private filterElement!: ElementRef<HTMLInputElement>;
  protected isScrolled: boolean = false;
  protected isSearch: boolean = false;
  protected isLoading: boolean = true;
  protected medias: MediaResponse[] = [];
  protected actors: ActorResponse[] = [];
  public clickedOutside: boolean = true;

  constructor(private mediaService: MediaService, private sanitizer: DomSanitizer,
              private elementRef: ElementRef) {
  }

  public findPaginatedMediaByTitle(filterValue: string): void {
    this.mediaService.findPaginatedMediaByTitle(0, 4, ['createdAt', 'DESC'], filterValue).subscribe({
      next: (data: any): void => {
        this.medias = data.content;
        this.getActors(data.content)
        this.produceMediaPoster(data.content);
      },
      error: (err: any): void => console.error(err),
      complete: (): boolean => this.isLoading = false
    })
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

  public getActors(medias: MediaResponse[]): void {
    medias.forEach((media: MediaResponse): void => {
      const movieId: string | null = media.id || null;
      if (movieId)
        this.mediaService.getActorsByMediaId(movieId).subscribe({
          next: (data: ActorResponse[]): ActorResponse[] => media.actors = data,
          error: (err: any): void => console.error(err)
        });
    });
  }

  public applyFilter(): void {
    const filterValue: string = this.filterElement.nativeElement.value.trim().toLowerCase();
    if (filterValue.length > 0) {
      this.isSearch = true;
      this.findPaginatedMediaByTitle(filterValue);
    } else this.isSearch = false;
  }

  public onError(event: Event): void {
    (event.target as HTMLImageElement).src = './assets/icons/onerror.png';
  }

  @HostListener('document:click', ['$event'])
  public onClick(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.medias = [];
      this.isSearch = false;
      this.filterElement.nativeElement.value = '';
    } else this.clickedOutside = false;
  }

  @HostListener('window:scroll', [])
  public onWindowScroll(): void {
    this.isScrolled = window.scrollY > 25;
  }
}
