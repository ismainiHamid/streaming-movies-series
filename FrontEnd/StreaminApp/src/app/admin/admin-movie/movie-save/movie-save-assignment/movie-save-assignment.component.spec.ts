import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieSaveAssignmentComponent } from './movie-save-assignment.component';

describe('MovieSaveAssignmentComponent', () => {
  let component: MovieSaveAssignmentComponent;
  let fixture: ComponentFixture<MovieSaveAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MovieSaveAssignmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovieSaveAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
