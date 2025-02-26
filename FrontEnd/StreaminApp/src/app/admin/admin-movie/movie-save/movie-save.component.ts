import {Component, HostListener} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-movie-save',
    templateUrl: './movie-save.component.html',
    styleUrl: './movie-save.component.css',
    standalone: false
})
export class MovieSaveComponent {


  constructor(private router: Router) {
  }

  public closeModal(): void {
    this.router.navigate(['/admin/movies']).then();
  }
}
