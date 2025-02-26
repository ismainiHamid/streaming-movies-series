import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieSaveVideoComponent } from './movie-save-video.component';

describe('MovieSaveVideoComponent', () => {
  let component: MovieSaveVideoComponent;
  let fixture: ComponentFixture<MovieSaveVideoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MovieSaveVideoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovieSaveVideoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
