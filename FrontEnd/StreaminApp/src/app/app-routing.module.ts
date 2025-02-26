import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AdminMainComponent} from './admin/layout/admin-main/admin-main.component';
import {AdminUserComponent} from './admin/admin-user/admin-user.component';
import {AdminMovieComponent} from './admin/admin-movie/admin-movie.component';
import {MovieSaveComponent} from './admin/admin-movie/movie-save/movie-save.component';
import {MovieSaveInfoComponent} from './admin/admin-movie/movie-save/movie-save-info/movie-save-info.component';
import {
  MovieSaveAssignmentComponent
} from './admin/admin-movie/movie-save/movie-save-assignment/movie-save-assignment.component';
import {MovieSaveVideoComponent} from './admin/admin-movie/movie-save/movie-save-video/movie-save-video.component';
import {MovieDeleteComponent} from './admin/admin-movie/movie-delete/movie-delete.component';
import {UserHomeComponent} from './user/user-home/user-home.component';
import {WatchMediaComponent} from './user/user-watch/watch-media/watch-media.component';
import {WatchDetailsComponent} from './user/user-watch/watch-details/watch-details.component';
import {UserMainComponent} from './user/layout/user-main/user-main.component';

const routes: Routes = [
  {
    path: '', component: UserMainComponent, children: [
      {path: '', component: UserHomeComponent},
      {path: 'details', component: WatchDetailsComponent},
    ]
  },
  {path: 'watch', component: WatchMediaComponent},
  {
    path: 'admin', component: AdminMainComponent, children: [
      {
        path: 'movies', component: AdminMovieComponent, children: [
          {
            path: 'save', component: MovieSaveComponent, children: [
              {path: '', component: MovieSaveInfoComponent},
              {path: 'assignment', component: MovieSaveAssignmentComponent},
              {path: 'upload', component: MovieSaveVideoComponent}
            ]
          },
          {path: 'delete', component: MovieDeleteComponent}
        ]
      },
      {
        path: 'users', component: AdminUserComponent, children: []
      },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
