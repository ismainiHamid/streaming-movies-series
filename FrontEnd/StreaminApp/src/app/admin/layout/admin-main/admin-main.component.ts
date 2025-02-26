import {Component} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs';

@Component({
    selector: 'app-admin-main',
    templateUrl: './admin-main.component.html',
    styleUrl: './admin-main.component.css',
    standalone: false
})
export class AdminMainComponent {
  protected isSidebarCollapsed = true;
  protected image: any;
  protected pageName: string = '';

  constructor(private router: Router) {
    this.extractPageName();
  }

  public extractPageName(): void {
    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(() => {
      const segments = this.router.url.split('/').filter(segment => segment);
      this.pageName = segments.length > 1 ? segments[1] : '';
    });
  }

  public toggleSidebar(): void {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
  }
}
