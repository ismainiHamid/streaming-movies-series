import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {NgOptimizedImage} from '@angular/common';
import {TruncatePipe} from "./shared/pipes/truncate.pipe";
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {MatButton, MatButtonModule, MatMiniFabButton} from '@angular/material/button';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AdminMainComponent} from './admin/layout/admin-main/admin-main.component';
import {MatPaginator} from '@angular/material/paginator';
import {MatFormField, MatFormFieldModule, MatLabel} from '@angular/material/form-field';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef, MatNoDataRow,
  MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatInput, MatInputModule} from '@angular/material/input';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatProgressBar} from '@angular/material/progress-bar';
import {AdminUserComponent} from './admin/admin-user/admin-user.component';
import {AdminProfileComponent} from './admin/admin-profile/admin-profile.component';
import {CarouselModule} from 'ngx-owl-carousel-o';
import {AdminMovieComponent} from './admin/admin-movie/admin-movie.component';
import {MovieSaveComponent} from './admin/admin-movie/movie-save/movie-save.component';
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from '@angular/material/autocomplete';
import {
  MatStep,
  MatStepLabel,
  MatStepper,
  MatStepperModule,
  MatStepperNext,
  MatStepperPrevious
} from '@angular/material/stepper';
import {MovieSaveInfoComponent} from './admin/admin-movie/movie-save/movie-save-info/movie-save-info.component';
import {
  MovieSaveAssignmentComponent
} from './admin/admin-movie/movie-save/movie-save-assignment/movie-save-assignment.component';
import {MovieSaveVideoComponent} from './admin/admin-movie/movie-save/movie-save-video/movie-save-video.component';
import {MovieDeleteComponent} from './admin/admin-movie/movie-delete/movie-delete.component';
import { UserHomeComponent } from './user/user-home/user-home.component';
import { HomeMediaListComponent } from './user/user-home/home-media-list/home-media-list.component';
import { WatchMediaComponent } from './user/user-watch/watch-media/watch-media.component';
import { WatchDetailsComponent } from './user/user-watch/watch-details/watch-details.component';
import { UserNavbarComponent } from './user/layout/user-navbar/user-navbar.component';
import { UserMainComponent } from './user/layout/user-main/user-main.component';
import { UserFooterComponent } from './user/layout/user-footer/user-footer.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminMainComponent,
    AdminUserComponent,
    AdminProfileComponent,
    AdminMovieComponent,
    MovieSaveComponent,
    MovieSaveInfoComponent,
    MovieSaveAssignmentComponent,
    MovieSaveVideoComponent,
    MovieDeleteComponent,
    UserHomeComponent,
    HomeMediaListComponent,
    WatchMediaComponent,
    WatchDetailsComponent,
    UserNavbarComponent,
    UserMainComponent,
    UserFooterComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgOptimizedImage,
    TruncatePipe,
    MatButton,
    ReactiveFormsModule,
    FormsModule,
    MatPaginator,
    MatFormField,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatRowDef,
    MatHeaderCellDef,
    MatCellDef,
    MatInput,
    MatMiniFabButton,
    MatProgressSpinner,
    MatNoDataRow,
    MatProgressBar,
    CarouselModule,
    MatAutocompleteTrigger,
    MatAutocomplete,
    MatOption,
    MatStep,
    MatStepper,
    MatStepLabel,
    MatStepperNext,
    MatStepperPrevious,
    MatLabel,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
