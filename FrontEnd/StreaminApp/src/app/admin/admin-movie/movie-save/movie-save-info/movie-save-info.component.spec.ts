import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieSaveInfoComponent } from './movie-save-info.component';

describe('MovieSaveInfoComponent', () => {
  let component: MovieSaveInfoComponent;
  let fixture: ComponentFixture<MovieSaveInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MovieSaveInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovieSaveInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
